package app.vercors.instance.launch

import app.vercors.APP_NAME
import app.vercors.APP_VERSION
import app.vercors.account.AccountData
import app.vercors.account.AccountService
import app.vercors.account.auth.AuthenticationService
import app.vercors.configuration.ConfigurationService
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceService
import app.vercors.instance.InstanceStatus
import app.vercors.instance.mojang.MojangService
import app.vercors.instance.mojang.data.*
import app.vercors.system.storage.StorageService
import app.vercors.unzipFile
import app.vercors.validate
import com.sun.jna.Platform
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import java.time.Duration
import java.time.Instant
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.absolutePathString
import kotlin.io.path.inputStream

private val logger = KotlinLogging.logger { }

class LauncherServiceImpl(
    coroutineScope: CoroutineScope,
    private val json: Json,
    private val storageService: StorageService,
    private val mojangService: MojangService,
    private val accountService: AccountService,
    private val authenticationService: AuthenticationService,
    private val instanceService: InstanceService,
    private val configurationService: ConfigurationService
) : LauncherService, CoroutineScope by coroutineScope {
    override fun launchInstance(instance: InstanceData, context: CoroutineContext) {
        launch {
            val selectedAccount = accountService.selectedAccountState.value
            instance.updateProgress(1)
            val preparation = runCatching { prepareInstance(instance, selectedAccount) }
            instance.updateProgress(10)
            if (preparation.isFailure) {
                val e = preparation.exceptionOrNull()!!
                logger.error(e) { "An error occured while preparing to run instance ${instance.name}" }
                instance.update(InstanceStatus.Errored(e))
                return@launch
            }
            val run = runCatching {
                System.gc()
                val process = launchProcess(preparation.getOrThrow())
                instance.update(InstanceStatus.Running)
                withContext(context) {
                    var lastInstant = Instant.now()
                    while (process.isAlive) {
                        val now = Instant.now()
                        instanceService.updateInstance(
                            instance.copy(
                                lastPlayed = now,
                                timePlayed = instance.timePlayed + Duration.between(lastInstant, now)
                            )
                        )
                        lastInstant = now
                        delay(1000)
                    }
                }
                logger.info { "Instance ${instance.name} stopped" }
                instance.update(InstanceStatus.Stopped)
            }
            run.onFailure {
                logger.error(it) { "An error occured while running instance ${instance.name}" }
                instance.update(InstanceStatus.Errored(it))
            }
            System.gc()
        }
    }

    private fun InstanceData.updateProgress(progress: Int) = update(InstanceStatus.Launching(progress / 10f))

    private fun InstanceData.update(status: InstanceStatus) {
        instanceService.updateInstanceStatus(this, status)
    }

    private suspend fun prepareInstance(instance: InstanceData, selectedAccount: AccountData?): ProcessBuilder {
        logger.info { "Launching instance ${instance.name}" }
        val account = authenticationService.validateToken(selectedAccount)
        if (account == null) {
            val job = Job()
            //send(LaunchStatus.RequiresLogin(job)) TODO
            job.join()
            if (job.isCancelled) throw CancellationException("Login cancelled. You must log into an account in order to launch the game.")
        } else {
            accountService.addAccount(account)
        }
        logger.info { "Access token validated - preparing to launch instance" }
        instance.updateProgress(2)
        val versionInfo = validateVersionInfo(instance.gameVersion)
        instance.updateProgress(3)
        val clientJarPath = validateClientJar(versionInfo)
        instance.updateProgress(4)
        val nativesPath = storageService.getInstanceNativesPath(instance)
        instance.updateProgress(5)
        val libraryPaths = validateLibraries(versionInfo, nativesPath)
        instance.updateProgress(6)
        val assetIndex = validateAssetIndex(versionInfo)
        instance.updateProgress(7)
        validateAssets(assetIndex)
        instance.updateProgress(8)
        val logConfigPath =
            if (versionInfo.logging != null) validateLogConfig(versionInfo.logging.client) else null
        instance.updateProgress(9)
        val classpath = buildList {
            add(clientJarPath)
            addAll(libraryPaths)
            add(".")
        }.joinToString(";", "\"", "\"")
        return buildProcess(
            instance,
            versionInfo,
            classpath,
            logConfigPath,
            accountService.selectedAccountState.value!!
        )
    }

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

    private suspend fun validateLibraries(version: MojangVersionInfo, nativesPath: Path): List<String> =
        coroutineScope {
            logger.debug { "Validating libraries" }
            val context = Dispatchers.IO.limitedParallelism(configurationService.config.maximumParallelThreads)
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
                        val native = it.natives!!.getValue(os)
                        val nativeFile = it.downloads.classifiers!!.getValue(native)
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

    private suspend fun validateAssets(assetIndex: MojangAssetIndex) = coroutineScope {
        logger.debug { "Validating assets" }
        val context = Dispatchers.IO.limitedParallelism(configurationService.config.maximumParallelThreads)
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
        instance: InstanceData,
        versionInfo: MojangVersionInfo,
        classpath: String,
        logConfigPath: Path?,
        account: AccountData
    ): ProcessBuilder {
        val instancePath = storageService.getInstancePath(instance)
        val nativesPath = storageService.getInstanceNativesPath(instance).absolutePathString()
        val assetsPath = storageService.assetsPath.absolutePathString()
        val variables = mapOf(
            "auth_player_name" to account.name,
            "version_name" to versionInfo.id,
            "game_directory" to instancePath.absolutePathString(),
            "assets_root" to assetsPath,
            "assets_index_name" to versionInfo.assetIndex.id,
            "auth_uuid" to account.uuid,
            "auth_access_token" to account.tokenData.token,
            "clientid" to "", // TODO
            "auth_xuid" to "", // TODO
            "user_type" to "msa",
            "version_type" to versionInfo.type,
            "natives_directory" to nativesPath,
            "launcher_name" to APP_NAME,
            "launcher_version" to APP_VERSION,
            "classpath" to classpath,
            // Legacy
            "auth_session" to account.tokenData.token,
            "user_properties" to "{}",
            "uuid" to account.uuid,
            "accessToken" to account.tokenData.token,
            "game_assets" to assetsPath
        )
        val jvmArgs = if (versionInfo.arguments != null) getProcessArgs(versionInfo.arguments.jvm, variables) else null
        val gameArgs =
            if (versionInfo.arguments != null) getProcessArgs(versionInfo.arguments.game, variables) else null
        val logConfigArg =
            if (logConfigPath != null) versionInfo.logging!!.client.argument.replaceVariables(mapOf("path" to logConfigPath.absolutePathString())) else null
        val javaPath =
            if (versionInfo.javaVersion.majorVersion <= 8) configurationService.config.java8Path else configurationService.config.java17Path
        if (javaPath == null) throw JavaVersionException("Java path needs to be set for version ${versionInfo.javaVersion.majorVersion}")
        val args = buildList {
            add(Path.of(javaPath, "bin", "java").absolutePathString())
            add("-Djava.library.path=$nativesPath")
            logConfigArg?.let { add(it) }
            configurationService.config.defaultAllocatedRam?.let { add("-Xmx${it}M") }
            configurationService.config.jvmArguments.let { if (it.isNotBlank()) add(it) }
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
        return ProcessBuilder(args).directory(instancePath.toFile())
    }

    private fun getProcessArgs(args: List<MojangArgument>, variables: Map<String, String>): List<String> =
        args.flatMap {
            when (it) {
                is BasicArgument -> listOf(it.value)
                is ConditionalArgument -> if (it.rules.all(::isRuleValid)) it.value else emptyList()
            }
        }.map { it.replaceVariables(variables) }

    private suspend fun launchProcess(
        processBuilder: ProcessBuilder,
        context: CoroutineContext = Dispatchers.IO
    ): Process = withContext(context) {
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