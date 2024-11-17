package app.vercors.launcher.core.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

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
fun <T> AppCrossfade(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: @Composable (T) -> Unit
) {
    if (AppAnimations.current) {
        Crossfade(
            targetState = targetState,
            modifier = modifier,
            animationSpec = animationSpec,
            content = content
        )
    } else {
        Box(modifier) {
            content(targetState)
        }
    }
}

@Composable
fun appAnimateColorAsState(
    targetValue: Color
): State<Color> =
    if (AppAnimations.current) animateColorAsState(targetValue = targetValue) else immutableStateOf(targetValue)

@Composable
fun Modifier.appAnimateContentSize(
    animationSpec: FiniteAnimationSpec<IntSize> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntSize.VisibilityThreshold
    ),
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)? = null
): Modifier = if (AppAnimations.current) animateContentSize(
    animationSpec = animationSpec,
    finishedListener = finishedListener
) else this