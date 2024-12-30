/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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