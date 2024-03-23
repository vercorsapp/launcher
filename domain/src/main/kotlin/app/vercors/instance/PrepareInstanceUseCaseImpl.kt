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
import app.vercors.instance.mojang.*
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationManager
import app.vercors.system.StorageManager
import app.vercors.unzipFile
import app.vercors.validate
import com.sun.jna.Platform
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.inputStream

private val logger = KotlinLogging.logger {}

class PrepareInstanceUseCaseImpl(
    private val externalScope: CoroutineScope,
    private val json: Json,
    private val instanceRepository: InstanceRepository,
    private val accountRepository: AccountRepository,
    private val configurationRepository: ConfigurationRepository,
    private val mojangRepository: MojangRepository,
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val storageManager: StorageManager,
    private val dialogManager: DialogManager,
    private val navigationManager: NavigationManager
) : PrepareInstanceUseCase {
    override suspend fun invoke(instance: Instance): LaunchPreparationResult = externalScope.async {
        logger.info { "Preparing instance ${instance.data.name}" }
        val selectedAccount = accountRepository.selectedState.value
        var account: Account? = null
        validateTokenUseCase(selectedAccount).collect {
            when (it) {
                is TokenValidationState.Progress -> instance.updateRefreshingTokenProgress(it.progress)
                is TokenValidationState.Success -> account = it.account
                else -> { // Do nothing
                }
            }
        }
        if (account == null) {
            val job = Job()
            dialogManager.openDialog(DialogConfig.Login { job.complete() })
            job.join()
            if (job.isCancelled || accountRepository.selectedState.value == null) {
                logger.warn { "Login cancelled - aborting..." }
                instanceRepository.updateInstanceStatus(instance.id) { InstanceStatus.Stopped }
                throw CancellationException("Login cancelled")
            }
        } else {
            accountRepository.addAccount(account!!)
        }
        logger.info { "Access token validated - preparing to launch instance" }
        navigationManager.navigateTo(NavigationConfig.InstanceDetails(instance.id))
        instance.updatePreparingProgress(1)
        val versionInfo = validateVersionInfo(instance.data.gameVersion)
        instance.updatePreparingProgress(2)
        val clientJarPath = validateClientJar(versionInfo)
        instance.updatePreparingProgress(3)
        val nativesPath = storageManager.getInstanceNativesPath(instance.id)
        instance.updatePreparingProgress(4)
        val libraryPaths = validateLibraries(versionInfo, nativesPath)
        instance.updatePreparingProgress(5)
        val assetIndex = validateAssetIndex(versionInfo)
        instance.updatePreparingProgress(6)
        validateAssets(assetIndex)
        instance.updatePreparingProgress(7)
        val logConfigPath = versionInfo.logging?.let { validateLogConfig(it.client) }
        instance.updatePreparingProgress(8)
        LaunchPreparationResult(versionInfo, clientJarPath, libraryPaths, logConfigPath)
    }.await()

    private suspend fun validateVersionInfo(version: MojangVersionManifest.Version): MojangVersionInfo {
        logger.debug { "Validating version information" }
        val path = storageManager.getVersionJsonPath(version.id)
        validate(
            path = path,
            url = version.url,
            sha1 = version.sha1,
        )
        return path.inputStream().use { json.decodeFromStream(it) }
    }

    private suspend fun validateClientJar(version: MojangVersionInfo): String {
        logger.debug { "Validating client JAR" }
        val path = storageManager.getVersionJarPath(version.id)
        val file = version.downloads.client
        validate(
            path = path,
            url = file.url,
            sha1 = file.sha1,
            size = file.size
        )
        return path.absolutePathString()
    }

    private suspend fun validateLibraries(version: MojangVersionInfo, nativesPath: Path): List<String> =
        coroutineScope {
            logger.debug { "Validating libraries" }
            val context = Dispatchers.IO.limitedParallelism(configurationRepository.current.maximumParallelDownloads)
            version.libraries.filter { it.rules?.all(MojangVersionInfo.Rule::isValid) ?: true }.map {
                async(context) {
                    val file = it.downloads.artifact
                    if (file != null) {
                        val path = storageManager.getLibraryPath(file.path)
                        validate(
                            path = path,
                            url = file.url,
                            sha1 = file.sha1,
                            size = file.size
                        )
                        path.absolutePathString()
                    } else {
                        val os = when {
                            Platform.isWindows() -> "windows"
                            Platform.isMac() -> "osx"
                            else -> "linux"
                        }
                        val native = it.natives!!.getValue(os)
                        val nativeFile = it.downloads.classifiers!!.getValue(native)
                        val path = storageManager.getLibraryPath(nativeFile.path)
                        validate(
                            path = path,
                            url = nativeFile.url,
                            sha1 = nativeFile.sha1,
                            size = nativeFile.size
                        )
                        unzipFile(path, nativesPath, it.extract!!.exclude)
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

    private suspend fun validateAssetIndex(version: MojangVersionInfo): MojangAssetIndex {
        logger.debug { "Validating asset index" }
        val path = storageManager.getAssetIndexPath(version.assets)
        validate(
            path = path,
            url = version.assetIndex.url,
            sha1 = version.assetIndex.sha1,
            size = version.assetIndex.size
        )
        return path.inputStream().use { json.decodeFromStream(it) }
    }

    private suspend fun validateAssets(assetIndex: MojangAssetIndex) = coroutineScope {
        logger.debug { "Validating assets" }
        val context = Dispatchers.IO.limitedParallelism(configurationRepository.current.maximumParallelDownloads)
        assetIndex.objects.forEach { name, (sha1, size) ->
            launch(context) {
                val path = storageManager.getAssetPath(sha1)
                val url = mojangRepository.getAssetUrl(sha1, name)
                validate(
                    path = path,
                    url = url,
                    sha1 = sha1,
                    size = size
                )
            }
        }
    }

    private suspend fun validateLogConfig(client: MojangVersionInfo.Logging.Client): Path {
        logger.debug { "Validating log config" }
        val file = client.file
        val path = storageManager.getLogConfigPath(client.type, file.id)
        validate(
            path = path,
            url = file.url,
            sha1 = file.sha1,
            size = file.size
        )
        return path
    }

    private fun Instance.updateRefreshingTokenProgress(progress: Float) = instanceRepository.updateInstanceStatus(id) {
        InstanceStatus.RefreshingToken(progress)
    }

    private fun Instance.updatePreparingProgress(progress: Int) = instanceRepository.updateInstanceStatus(id) {
        InstanceStatus.Preparing(progress / 8f)
    }
}