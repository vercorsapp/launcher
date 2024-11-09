package app.vercors.launcher.core.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

val defaultEnterAnimation = fadeIn(animationSpec = tween(200))
val defaultExitAnimation = fadeOut(animationSpec = tween(200))
val defaultAnimation = defaultEnterAnimation togetherWith defaultExitAnimation

val AppAnimations = compositionLocalOf { true }

@Composable
fun <S> AppAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<S>.() -> ContentTransform = { defaultAnimation },
    contentAlignment: Alignment = Alignment.TopStart,
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable() (targetState: S) -> Unit
) {
    if (AppAnimations.current) {
        AnimatedContent(
            targetState = targetState,
            modifier = modifier,
            transitionSpec = transitionSpec,
            contentAlignment = contentAlignment,
            contentKey = contentKey
        ) {
            content(it)
        }
    } else {
        content(targetState)
    }
}

@Composable
fun AppAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = defaultEnterAnimation,
    exit: ExitTransition = defaultExitAnimation,
    content: @Composable() () -> Unit
) {
    if (AppAnimations.current) {
        AnimatedVisibility(
            visible = visible,
            modifier = modifier,
            enter = enter,
            exit = exit,
        ) {
            content()
        }
    } else if (visible) {
        content()
    }
}

@Composable
fun appAnimateColorAsState(
    targetValue: Color
): State<Color> =
    if (AppAnimations.current) animateColorAsState(targetValue = targetValue) else immutableStateOf(targetValue)