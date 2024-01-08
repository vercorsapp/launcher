package com.skyecodes.snowball.ui.util

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedTooltipArea(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    delayMillis: Int = 500,
    tooltipPlacement: TooltipPlacement = TooltipPlacement.CursorPoint(
        offset = DpOffset(0.dp, 16.dp)
    ),
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) {
    var parentBounds by remember { mutableStateOf(Rect.Zero) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    var cursorPosition by remember { mutableStateOf(Offset.Zero) }
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }

    fun startShowing() {
        job?.cancel()
        job = scope.launch {
            delay(delayMillis.toLong())
            isVisible = true
        }
    }

    fun hide() {
        job?.cancel()
        isVisible = false
    }

    fun hideIfNotHovered(globalPosition: Offset) {
        if (!parentBounds.contains(globalPosition)) {
            hide()
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { parentBounds = it.boundsInWindow() }
            .onPointerEvent(PointerEventType.Enter) {
                cursorPosition = it.position
                startShowing()
            }
            .onPointerEvent(PointerEventType.Move) {
                cursorPosition = it.position
                hideIfNotHovered(parentBounds.topLeft + it.position)
            }
            .onPointerEvent(PointerEventType.Exit) {
                hideIfNotHovered(parentBounds.topLeft + it.position)
            }
            .pointerInput(Unit) {
                detectDown {
                    hide()
                }
            }
    ) {
        content()
        if (isVisible) {
            @OptIn(ExperimentalFoundationApi::class)
            (Popup(
                popupPositionProvider = tooltipPlacement.positionProvider(cursorPosition),
                onDismissRequest = { isVisible = false }
            ) {
                Box(
                    Modifier
                        .onGloballyPositioned { popupPosition = it.positionInWindow() }
                        .onPointerEvent(PointerEventType.Move) {
                            hideIfNotHovered(popupPosition + it.position)
                        }
                        .onPointerEvent(PointerEventType.Exit) {
                            hideIfNotHovered(popupPosition + it.position)
                        }
                ) {
                    AnimatedVisibility(isVisible, enter = enter, exit = exit) {
                        tooltip()
                    }
                }
            })
        }
    }
}

private val PointerEvent.position get() = changes.first().position

private fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass = PointerEventPass.Main,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
) = pointerInput(eventType, pass, onEvent) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent(pass)
            if (event.type == eventType) {
                onEvent(event)
            }
        }
    }
}

private suspend fun PointerInputScope.detectDown(onDown: (Offset) -> Unit) {
    while (true) {
        awaitPointerEventScope {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val down = event.changes.find { it.changedToDown() }
            if (down != null) {
                onDown(down.position)
            }
        }
    }
}