package app.vercors.launcher.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    mainDispatcher: CoroutineDispatcher = koinInject(named("mainDispatcher")),
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(mainDispatcher) {
                flow.collect(onEvent)
            }
        }
    }
}