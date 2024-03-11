package app.vercors.instance

import androidx.compose.runtime.*
import app.vercors.project.ModLoader
import app.vercors.readable
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.lastPlayedTime
import vercors.app.generated.resources.notPlayedBefore

val InstanceData.loaderAndVersionString: String @Stable get() = "${loader?.value ?: ModLoader.Vanilla} ${gameVersion.id}"

val InstanceData.lastPlayedString: String
    @Composable get() {
        val lastPlayed = this.lastPlayed ?: return stringResource(Res.string.notPlayedBefore)
        var text by remember { mutableStateOf(lastPlayed.readable()) }
        var refresh by remember { mutableStateOf(false) }

        LaunchedEffect(refresh) {
            text = lastPlayed.readable()
            delay(10_000)
            refresh = !refresh
        }

        LaunchedEffect(lastPlayed) {
            text = lastPlayed.readable()
        }

        return stringResource(Res.string.lastPlayedTime, text)
    }