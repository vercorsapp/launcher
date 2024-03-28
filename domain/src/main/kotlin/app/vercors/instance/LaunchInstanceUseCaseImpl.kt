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

import app.vercors.APP_NAME
import app.vercors.APP_VERSION
import app.vercors.account.Account
import app.vercors.account.AccountRepository
import app.vercors.configuration.ConfigurationRepository
import app.vercors.dialog.DialogConfig
import app.vercors.dialog.DialogManager
import app.vercors.instance.mojang.*
import app.vercors.system.StorageManager
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import java.nio.file.Path
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.absolutePathString

private val logger = KotlinLogging.logger {}

class LaunchInstanceUseCaseImpl(
    private val externalScope: CoroutineScope,
    private val prepareInstanceUseCase: PrepareInstanceUseCase,
    private val accountRepository: AccountRepository,
    private val instanceRepository: InstanceRepository,
    private val configurationRepository: ConfigurationRepository,
    private val storageManager: StorageManager,
    private val dialogManager: DialogManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LaunchInstanceUseCase {
    override suspend fun invoke(instance: Instance) = externalScope.launch {
        val (versionInfo, clientJarPath, libraryPaths, logConfigPath) = prepareInstanceUseCase(instance)
        val wasStopped = AtomicBoolean(false)
        val wasKilled = AtomicBoolean(false)
        val run = runCatching {
            val selectedAccount = accountRepository.currentSelected
            val classpath = buildList {
                add(clientJarPath)
                addAll(libraryPaths)
                add(".")
            }.joinToString(";", "\"", "\"")
            val processBuilder = buildProcess(instance, versionInfo, classpath, logConfigPath, selectedAccount)
            System.gc()
            val process = launchProcess(processBuilder)
            instance.updateStatus(InstanceStatus.Running { onCancellation(instance, process, wasStopped, wasKilled) })
            instanceRepository.updateInstanceData(instance.id) { it.copy(lastPlayed = Instant.now()) }

            while (process.isAlive) {
                repeat(10) {
                    ensureActive()
                    delay(100)
                }
                instance.tickRunning()
            }

            logger.info { "Instance ${instance.data.name} stopped" }
            process.exitValue()
        }
        run.onFailure {
            when (it) {
                is JavaVersionException -> dialogManager.openDialog(
                    DialogConfig.Error.JavaVersion(instance.id, it.javaVersion)
                )

                else -> {
                    logger.error(it) { "An error occured while running instance ${instance.data.name}" }
                    dialogManager.openDialog(DialogConfig.Error.Launch)
                }
            }
        }
        instanceRepository.updateInstanceStatus(instance.id) {
            if (run.isSuccess && 0 == run.getOrNull()) InstanceStatus.NotRunning
            else if (wasKilled.get()) InstanceStatus.Killed
            else InstanceStatus.Crashed
        }
    }.join()

    private suspend fun onCancellation(
        instance: Instance,
        process: Process,
        wasStopped: AtomicBoolean,
        wasKilled: AtomicBoolean
    ) = withContext(ioDispatcher) {
        if (!wasStopped.get() && process.supportsNormalTermination()) {
            logger.info { "Stopping instance ${instance.data.name}" }
            wasStopped.set(true)
            process.destroy()
        } else {
            dialogManager.openDialog(DialogConfig.KillInstance {
                logger.info { "Killing instance ${instance.data.name}" }
                wasKilled.set(true)
                process.destroy()
            })
        }
    }

    private fun Instance.updateStatus(status: InstanceStatus) = instanceRepository.updateInstanceStatus(id) { status }

    private suspend fun Instance.tickRunning() {
        instanceRepository.updateInstanceData(id) {
            val now = Instant.now()
            it.copy(
                lastPlayed = now, timePlayed = it.timePlayed + Duration.between(it.lastPlayed, now)
            )
        }
    }

    private fun buildProcess(
        instance: Instance, versionInfo: MojangVersionInfo, classpath: String, logConfigPath: Path?, account: Account
    ): ProcessBuilder {
        val instancePath = storageManager.getInstancePath(instance.id)
        val nativesPath = storageManager.getInstanceNativesPath(instance.id).absolutePathString()
        val assetsPath = storageManager.assetsPath.absolutePathString()
        val variables = mapOf(
            "auth_player_name" to account.name,
            "version_name" to versionInfo.id,
            "game_directory" to instancePath.absolutePathString(),
            "assets_root" to assetsPath,
            "assets_index_name" to versionInfo.assetIndex.id,
            "auth_uuid" to account.uuid,
            "auth_access_token" to account.tokenData.token,
            "clientid" to "",
            "auth_xuid" to "",
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
        val jvmArgs = versionInfo.arguments?.let { getProcessArgs(it.jvm, variables) }
        val gameArgs = versionInfo.arguments?.let { getProcessArgs(it.game, variables) }
        val logConfigArg =
            if (logConfigPath != null) versionInfo.logging!!.client.argument.replaceVariables(mapOf("path" to logConfigPath.absolutePathString())) else null
        val javaPath =
            if (versionInfo.javaVersion.majorVersion <= 8) configurationRepository.current.java8Path else configurationRepository.current.java17Path
        if (javaPath.isNullOrBlank()) throw JavaVersionException(versionInfo.javaVersion.majorVersion)
        val args = buildList {
            add(Path.of(javaPath, "bin", "java").absolutePathString())
            add("-Djava.library.path=$nativesPath")
            logConfigArg?.let { add(it) }
            configurationRepository.current.defaultAllocatedRam?.let { add("-Xmx${it}M") }
            configurationRepository.current.jvmArguments.let { if (it.isNotBlank()) add(it) }
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
                is ConditionalArgument -> if (it.rules.all(MojangVersionInfo.Rule::isValid)) it.value else emptyList()
            }
        }.map { it.replaceVariables(variables) }

    private fun launchProcess(processBuilder: ProcessBuilder): Process {
        logger.info { "Launching game" }
        logger.debug { "Command arguments: ${processBuilder.command()}" }
        return processBuilder.inheritIO().start()
    }

    private fun String.replaceVariables(variables: Map<String, String>) =
        variables.entries.fold(this) { s, (key, value) ->
            s.replace("\${$key}", value)
        }
}