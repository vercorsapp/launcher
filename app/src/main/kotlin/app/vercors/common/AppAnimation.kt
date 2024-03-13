package app.vercors.common

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import app.vercors.LocalConfiguration
import app.vercors.applyIf

@Composable
fun AppAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    label: String = "AppAnimatedVisibility",
    content: @Composable () -> Unit
) {
    if (LocalConfiguration.current.animations) {
        AnimatedVisibility(visible, modifier, enter, exit, label) { content() }
    } else if (visible) {
        content()
    }
}

@Composable
fun appAnimateColorAsState(
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = spring(),
    label: String = "AppColorAnimation",
    finishedListener: ((Color) -> Unit)? = null
): State<Color> {
    return if (LocalConfiguration.current.animations) {
        animateColorAsState(targetValue, animationSpec, label, finishedListener)
    } else {
        derivedStateOf { targetValue }
    }
}

@Composable
@NonRestartableComposable
fun appAnimateColorAsState(
    condition: Boolean,
    valueIfTrue: Color,
    valueIfFalse: Color,
    animationSpec: AnimationSpec<Color> = spring(),
    label: String = "AppColorAnimation",
    finishedListener: ((Color) -> Unit)? = null
): State<Color> = appAnimateColorAsState(
    targetValue = if (condition) valueIfTrue else valueIfFalse,
    animationSpec = animationSpec,
    label = label,
    finishedListener = finishedListener
)

@Composable
fun Modifier.appAnimateContentSize(
    animationSpec: FiniteAnimationSpec<IntSize> = spring(
        stiffness = Spring.StiffnessMediumLow
    ),
    finishedListener: ((initialValue: IntSize, targetValue: IntSize) -> Unit)? = null
): Modifier = applyIf(LocalConfiguration.current.animations) { animateContentSize(animationSpec, finishedListener) }