package app.vercors.launcher.core.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

val defaultEnterAnimation = fadeIn(animationSpec = tween(200))
val defaultExitAnimation = fadeOut(animationSpec = tween(200))
val defaultAnimation = defaultEnterAnimation togetherWith defaultExitAnimation