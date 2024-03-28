/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.instance

import app.vercors.account.Account
import app.vercors.account.AccountRepository
import app.vercors.account.auth.TokenValidationState
import app.vercors.account.auth.ValidateTokenUseCase
import app.vercors.configuration.ConfigurationRepository
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogManager
import app.vercors.instance.mojang.MojangAssetIndex
import app.vercors.instance.mojang.MojangRepository
import app.vercors.instance.mojang.MojangVersionInfo
import app.vercors.instance.mojang.isValid
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationManager
import app.vercors.system.StorageManager
import com.sun.jna.Platform
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URL
import java.nio.file.Path
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.*

private val logger = KotlinLogging.logger {}

class PrepareInstanceUseCaseImpl(
    private val externalScope: CoroutineScope,
    private val json: Json,
    private val httpClient: HttpClient,
    private val instanceRepository: InstanceRepository,
    private val accountRepository: AccountRepository,
    private val configurationRepository: ConfigurationRepository,
    private val mojangRepository: MojangRepository,
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val storageManager: StorageManager,
    private val dialogManager: DialogManager,
    private val navigationManager: NavigationManager,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PrepareInstanceUseCase {
    override suspend fun invoke(instance: Instance): LaunchPreparationResult = externalScope.async {
        val verificationContext = defaultDispatcher + SupervisorJob()
        val extractionContext = defaultDispatcher + SupervisorJob()
        val downloadContext =
            Dispatchers.IO.limitedParallelism(configurationRepository.current.maximumParallelDownloads) + SupervisorJob()
        logger.info { "Preparing instance ${instance.data.name}" }
        validateAccessToken(instance)
        logger.info { "Access token validated - preparing to launch instance" }
        navigationManager.navigateTo(NavigationConfig.InstanceDetails(instance.id))
        instance.updateStatus(InstanceStatus.Preparing)
        val versionInfo = mojangRepository.getVersionInfo(instance.data.gameVersion)
        val assetIndex = mojangRepository.getAssetIndex(versionInfo)
        val clientJarPath = storageManager.getVersionJarPath(versionInfo.id)
        val logConfigPath =
            versionInfo.logging?.let { storageManager.getLogConfigPath(it.client.type, it.client.file.id) }
        val filesToVerify = getFilesToVerify(instance, versionInfo, assetIndex, clientJarPath, logConfigPath)

        instance.updateStatus(InstanceStatus.Verifying(filesToVerify.size))
        val verificationResult = filesToVerify.map {
            async(verificationContext) {
                verifyFile(it, instance)
            }
        }.awaitAll().filterNotNull()
        val verificationFailures =
            verificationResult.filter { it.second is FileVerificationResult.Failure }.map { it.first }
        logger.info { "Verification complete" }

        val filesToDownload = verificationResult
            .filter { it.second is FileVerificationResult.RequiresDownload }
            .map { it.first }
        val downloadResult = filesToDownload.map {
            async(downloadContext) {
                downloadFile(it, instance)
            }
        }.awaitAll().filterNotNull()
        val downloadFailures = downloadResult.map { it.first }
        if (filesToDownload.isNotEmpty()) logger.info { "Download complete" }

        instance.updateStatus(InstanceStatus.Extracting)
        val filesToExtract = filesToVerify.filterIsInstance<DownloadableFile.Native>()
        val extractionResult = filesToExtract.map {
            async(extractionContext) {
                extractFile(it)
            }
        }.awaitAll().filterNotNull()
        if (filesToExtract.isNotEmpty()) logger.info { "Extraction complete" }

        // TODO better error handling
        verificationFailures.forEach { logger.error { "Failure to verify file ${it.path}" } }
        downloadFailures.forEach { logger.error { "Failure to download file ${it.url}" } }
        extractionResult.forEach { (file, e) -> logger.error(e) { "Failure to extract file ${file.path}" } }

        LaunchPreparationResult(
            versionInfo,
            clientJarPath.absolutePathString(),
            filesToVerify.filterIsInstance<DownloadableFile.Library>().map { it.path.absolutePathString() },
            logConfigPath
        )
    }.await()

    private suspend fun validateAccessToken(instance: Instance) {
        val selectedAccount = accountRepository.selectedState.value
        var account: Account? = null
        validateTokenUseCase(selectedAccount).collect {
            when (it) {
                is TokenValidationState.Progress -> instance.updateStatus(InstanceStatus.RefreshingToken(it.progress))
                is TokenValidationState.Success -> account = it.account
                TokenValidationState.FullLoginRequired -> account = null
            }
        }
        if (account == null) {
            val job = Job()
            dialogManager.openDialog(DialogConfig.Login { job.complete() })
            job.join()
            if (job.isCancelled || accountRepository.selectedState.value == null) {
                logger.warn { "Login cancelled - aborting..." }
                instanceRepository.updateInstanceStatus(instance.id) { InstanceStatus.NotRunning }
                throw CancellationException("Login cancelled")
            }
        } else {
            accountRepository.addAccount(account!!)
        }
    }

    private fun getFilesToVerify(
        instance: Instance,
        version: MojangVersionInfo,
        assetIndex: MojangAssetIndex,
        clientJarPath: Path,
        logConfigPath: Path?
    ): List<DownloadableFile> = buildList {
        add(DownloadableFile.Client(clientJarPath, version.downloads.client))
        addAll(getLibraryFiles(instance, version))
        addAll(getAssetFiles(assetIndex))
        logConfigPath?.let { add(DownloadableFile.LogConfig(it, version.logging!!.client.file)) }
    }

    private fun getLibraryFiles(
        instance: Instance,
        version: MojangVersionInfo
    ): List<DownloadableFile> {
        val nativesPath = storageManager.getInstanceNativesPath(instance.id)
        return version.libraries
            .filter { it.rules?.all(MojangVersionInfo.Rule::isValid) ?: true }
            .map {
                val file = it.downloads.artifact
                if (file != null) {
                    val path = storageManager.getLibraryPath(file.path)
                    DownloadableFile.Library(path, file)
                } else {
                    val os = when {
                        Platform.isWindows() -> "windows"
                        Platform.isMac() -> "osx"
                        else -> "linux"
                    }
                    val native = it.natives!!.getValue(os)
                    val nativeFile = it.downloads.classifiers!!.getValue(native)
                    val path = storageManager.getLibraryPath(nativeFile.path)
                    DownloadableFile.Native(path, nativeFile, nativesPath, it.extract!!.exclude)
                }
            }
    }

    private fun getAssetFiles(assetIndex: MojangAssetIndex): List<DownloadableFile> =
        assetIndex.objects.map { (name, assertObj) ->
            val path = storageManager.getAssetPath(assertObj.hash)
            val url = mojangRepository.getAssetUrl(assertObj.hash, name)
            DownloadableFile.Asset(path, url, assertObj.size, assertObj.hash)
        }

    private fun verifyFile(
        file: DownloadableFile,
        instance: Instance
    ): Pair<DownloadableFile, FileVerificationResult>? = runCatching {
        logger.trace { "Verifying file at location ${file.path}" }
        val result = file.verify()
        instance.incrementVerifyingStatus()
        return if (result != FileVerificationResult.Success) file to result else null
    }.getOrElse {
        if (it is Exception) file to FileVerificationResult.Failure.Unknown(it) else throw it
    }

    private fun downloadFile(
        file: DownloadableFile,
        instance: Instance
    ): Pair<DownloadableFile, DownloadResult.Failure>? = runCatching {
        logger.trace { "Starting download of file ${file.url}" }
        val url = URL(file.url)
        val connection = url.openConnection()
        val size = connection.contentLength
        if (size != file.size) return@runCatching file to DownloadResult.Failure.SizeMismatch
        val md = MessageDigest.getInstance("SHA1")
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytesCopied = 0
        connection.getInputStream().use { input ->
            file.path.deleteRecursively()
            file.path.createParentDirectories().outputStream().use { output ->
                var bytes = input.read(buffer)
                while (bytes >= 0) {
                    output.write(buffer, 0, bytes)
                    md.update(buffer, 0, bytes)
                    bytesCopied += bytes
                    instance.incrementDownloadingStatus(bytes)
                    bytes = input.read(buffer)
                }
            }
        }
        val actualSha1 = md.digest().toHexString()
        if (actualSha1 != file.sha1) return@runCatching file to DownloadResult.Failure.HashMismatch
        null
    }.getOrElse {
        if (it is Exception) file to DownloadResult.Failure.Unknown(it) else throw it
    }

    private fun extractFile(file: DownloadableFile.Native): Pair<DownloadableFile.Native, Exception>? = runCatching {
        logger.trace { "Extracting file ${file.path} to location ${file.dest}" }
        val zis = ZipInputStream(FileInputStream(file.path.toFile()))
        zis.use {
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                if (file.excludes.none { zipEntry!!.name.startsWith(it) }) {
                    processZipEntry(zis, zipEntry, file.dest)
                }
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
        }
        null
    }.getOrElse {
        if (it is Exception) file to it else throw it
    }

    private fun processZipEntry(
        zis: ZipInputStream,
        zipEntry: ZipEntry,
        dest: Path,
    ) {
        val newFile = createFileForZipEntry(dest, zipEntry)
        if (zipEntry.isDirectory) {
            if (!newFile.isDirectory()) newFile.createDirectories()
        } else {
            val parent = newFile.parent
            if (!parent.isDirectory()) parent.createDirectories()
            newFile.outputStream().use { zis.copyTo(it) }
        }
    }

    private fun createFileForZipEntry(
        destinationDir: Path,
        zipEntry: ZipEntry,
    ): Path {
        val destFile = destinationDir.resolve(zipEntry.name)
        val destDirPath = destinationDir.absolutePathString()
        val destFilePath = destFile.absolutePathString()
        if (!destFilePath.startsWith(destDirPath + File.separator))
            throw IOException("Entry is outside of the target dir: " + zipEntry.name)
        return destFile
    }

    private fun Instance.incrementVerifyingStatus() = instanceRepository.updateInstanceStatus(id) {
        if (it is InstanceStatus.Verifying) it.copy(current = it.current + 1) else it
    }

    private fun Instance.incrementDownloadingStatus(amount: Int) = instanceRepository.updateInstanceStatus(id) {
        if (it is InstanceStatus.Downloading) it.copy(current = it.current + amount) else it
    }

    private fun Instance.updateStatus(status: InstanceStatus) = instanceRepository.updateInstanceStatus(id) { status }

    private fun DownloadableFile.verify(): FileVerificationResult = when {
        !path.exists() -> FileVerificationResult.RequiresDownload
        path.fileSize().toInt() != size -> FileVerificationResult.Failure.SizeMismatch
        path.sha1() != sha1 -> FileVerificationResult.Failure.HashMismatch
        else -> FileVerificationResult.Success
    }

    private fun Path.sha1(): String {
        val md = MessageDigest.getInstance("SHA1")
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        DigestInputStream(inputStream(), md).use { while (it.read(buffer) != -1); }
        return md.digest().toHexString()
    }
}
