package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.APP_NAME
import com.skyecodes.vercors.APP_VERSION
import com.skyecodes.vercors.data.model.app.Account
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.mojang.*
import com.skyecodes.vercors.unzipFile
import com.skyecodes.vercors.validate
import com.sun.jna.Platform
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import java.time.Duration
import java.time.Instant
import kotlin.io.path.absolutePathString
import kotlin.io.path.inputStream

private val logger = KotlinLogging.logger { }

interface LauncherService {
    fun launch(instance: Instance): Flow<LaunchStatus>
}

sealed interface LaunchStatus {
    data class RequiresLogin(val job: CompletableJob) : LaunchStatus
    data class Errored(val error: Throwable) : LaunchStatus
    data class Preparing(val progress: Float) : LaunchStatus
    data object Running : LaunchStatus
    data object Stopped : LaunchStatus
}

class LauncherServiceImpl(
    coroutineScope: CoroutineScope,
    private val json: Json,
    private val storageService: StorageService,
    private val mojangService: MojangService,
    private val accountService: AccountService,
    private val instanceService: InstanceService
) : LauncherService, CoroutineScope by coroutineScope {
    override fun launch(instance: Instance): Flow<LaunchStatus> = channelFlow {
        val preparation = runCatching {
            logger.info { "Launching instance ${instance.name}" }
            if (!accountService.validateToken()) {
                val job = Job()
                send(LaunchStatus.RequiresLogin(job))
                job.join()
                if (job.isCancelled) {
                    send(LaunchStatus.Errored(CancellationException("Login cancelled. You must log into an account in order to launch the game.")))
                    return@channelFlow
                }
            }
            logger.info { "Access token validated - preparing to launch instance" }
            send(LaunchStatus.Preparing(0f))
            val versionInfo = validateVersionInfo(instance.gameVersion)
            val clientJarPath = validateClientJar(versionInfo)
            val nativesPath = storageService.getInstanceNativesDir(instance)
            val libraryPaths = validateLibraries(versionInfo, nativesPath)
            val assetIndex = validateAssetIndex(versionInfo)
            validateAssets(assetIndex)
            val logConfigPath = if (versionInfo.logging != null) validateLogConfig(versionInfo.logging.client) else null
            val classpath = buildList {
                add(clientJarPath)
                addAll(libraryPaths)
                add(".")
            }.joinToString(";", "\"", "\"")
            val account = accountService.selectedAccount.firstOrNull()
            buildProcess(instance, versionInfo, classpath, logConfigPath, account!!)
        }
        if (preparation.isFailure) {
            val e = preparation.exceptionOrNull()!!
            logger.error(e) { "An error occured while running instance ${instance.name}" }
            send(LaunchStatus.Errored(e))
            return@channelFlow
        }
        val run = runCatching {
            System.gc()
            val process = launchProcess(preparation.getOrThrow())
            send(LaunchStatus.Running)
            withContext(Dispatchers.IO) {
                var lastInstant = Instant.now()
                while (process.isAlive) {
                    delay(1000)
                    val now = Instant.now()
                    instanceService.updateInstance(
                        instance.copy(
                            lastPlayed = lastInstant,
                            timePlayed = instance.timePlayed + Duration.between(lastInstant, now)
                        )
                    )
                    lastInstant = now
                }
            }
            logger.info { "Instance ${instance.name} stopped" }
            send(LaunchStatus.Stopped)
        }
        run.onFailure {
            logger.error(it) { "An error occured while running instance ${instance.name}" }
            send(LaunchStatus.Errored(it))
        }
        System.gc()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun validateVersionInfo(version: MojangVersionManifest.Version): MojangVersionInfo {
        logger.debug { "Validating version information" }
        val path = storageService.getVersionJsonPath(version.id)
        validate(
            path = path,
            url = version.url,
            sha1 = version.sha1,
        )
        return path.inputStream().use { json.decodeFromStream(it) }
    }

    private suspend fun validateClientJar(version: MojangVersionInfo): String {
        logger.debug { "Validating client JAR" }
        val path = storageService.getVersionJarPath(version.id)
        val file = version.downloads.client
        validate(
            path = path,
            url = file.url,
            sha1 = file.sha1,
            size = file.size
        )
        return path.absolutePathString()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun validateLibraries(version: MojangVersionInfo, nativesPath: Path): List<String> =
        coroutineScope {
            logger.debug { "Validating libraries" }
            val context = Dispatchers.IO.limitedParallelism(8)
            version.libraries.filter { it.rules?.all(::isRuleValid) ?: true }.map {
                async(context) {
                    val file = it.downloads.artifact
                    if (file != null) {
                        val path = storageService.getLibraryPath(file.path)
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
                        val native = it.natives!![os]
                        val nativeFile = it.downloads.classifiers!![native]!!
                        val path = storageService.getLibraryPath(nativeFile.path)
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

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun validateAssetIndex(version: MojangVersionInfo): MojangAssetIndex {
        logger.debug { "Validating asset index" }
        val path = storageService.getAssetIndexPath(version.assets)
        validate(
            path = path,
            url = version.assetIndex.url,
            sha1 = version.assetIndex.sha1,
            size = version.assetIndex.size
        )
        return path.inputStream().use { json.decodeFromStream(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun validateAssets(assetIndex: MojangAssetIndex) = coroutineScope {
        logger.debug { "Validating assets" }
        val context = Dispatchers.IO.limitedParallelism(8)
        assetIndex.objects.forEach { name, (sha1, size) ->
            launch(context) {
                val path = storageService.getAssetPath(sha1)
                val url = mojangService.getAssetUrl(sha1, name)
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
        val path = storageService.getLogConfigPath(client.type, file.id)
        validate(
            path = path,
            url = file.url,
            sha1 = file.sha1,
            size = file.size
        )
        return path
    }

    private fun buildProcess(
        instance: Instance,
        versionInfo: MojangVersionInfo,
        classpath: String,
        logConfigPath: Path?,
        account: Account
    ): ProcessBuilder {
        val instanceDir = storageService.getInstanceDir(instance)
        val nativesDir = storageService.getInstanceNativesDir(instance).absolutePathString()
        val assetsDir = storageService.assetsDir.absolutePathString()
        val variables = mapOf(
            "auth_player_name" to account.name,
            "version_name" to versionInfo.id,
            "game_directory" to instanceDir.absolutePathString(),
            "assets_root" to assetsDir,
            "assets_index_name" to versionInfo.assetIndex.id,
            "auth_uuid" to account.uuid,
            "auth_access_token" to account.tokenData.token,
            "clientid" to "", // TODO
            "auth_xuid" to "", // TODO
            "user_type" to "msa", // TODO
            "version_type" to versionInfo.type,
            "natives_directory" to nativesDir,
            "launcher_name" to APP_NAME,
            "launcher_version" to APP_VERSION,
            "classpath" to classpath,
            // Legacy
            "auth_session" to account.tokenData.token,
            "user_properties" to "{}",
            "uuid" to account.uuid,
            "accessToken" to account.tokenData.token,
            "game_assets" to assetsDir
        )
        val jvmArgs = if (versionInfo.arguments != null) getProcessArgs(versionInfo.arguments.jvm, variables) else null
        val gameArgs =
            if (versionInfo.arguments != null) getProcessArgs(versionInfo.arguments.game, variables) else null
        val logConfigArg =
            if (logConfigPath != null) versionInfo.logging!!.client.argument.replaceVariables(mapOf("path" to logConfigPath.absolutePathString())) else null
        val args = buildList {
            add("C:\\Program Files\\Java\\jre-1.8\\bin\\java.exe")
            add("-Djava.library.path=$nativesDir")
            logConfigArg?.let { add(it) }
            jvmArgs?.let { addAll(it) }
            versionInfo.minecraftArguments?.let {
                add("-cp")
                add(classpath)
            }
            add(versionInfo.mainClass)
            versionInfo.minecraftArguments?.let {
                addAll(it.split(' ').map { arg -> arg.replaceVariables(variables) })
            }
            gameArgs?.let { addAll(it) }
        }
        return ProcessBuilder(args).directory(instanceDir.toFile())
    }

    private fun getProcessArgs(args: List<MojangArgument>, variables: Map<String, String>): List<String> =
        args.flatMap {
            when (it) {
                is BasicArgument -> listOf(it.value)
                is ConditionalArgument -> if (it.rules.all(::isRuleValid)) it.value else emptyList()
            }
        }.map { it.replaceVariables(variables) }

    private suspend fun launchProcess(processBuilder: ProcessBuilder): Process = withContext(Dispatchers.IO) {
        logger.info { "Launching game" }
        logger.debug { "Command arguments: ${processBuilder.command()}" }
        processBuilder.inheritIO().start()
    }

    private fun isRuleValid(rule: MojangVersionInfo.Rule): Boolean {
        val isOsValid = rule.os == null || (when (rule.os.name) {
            "windows" -> Platform.isWindows()
            "linux" -> Platform.isLinux()
            "osx" -> Platform.isMac()
            else -> true
        } && when (rule.os.arch) {
            "x86" -> !Platform.is64Bit()
            else -> true
        } && (rule.os.version == null || System.getProperty("os.version").matches(rule.os.version.toRegex())))
        val isFeaturesValid = rule.features == null // TODO handle feature rules
        val isValid = isOsValid && isFeaturesValid
        return if (rule.action == "allow") isValid else !isValid
    }

    private fun String.replaceVariables(variables: Map<String, String>) =
        variables.entries.fold(this) { s, (key, value) ->
            s.replace("\${$key}", value)
        }
}
