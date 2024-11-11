package app.vercors.launcher.core.presentation.ui

import androidx.compose.runtime.compositionLocalOf
import coil3.ImageLoader
import coil3.PlatformContext

val LocalImageLoader = compositionLocalOf { ImageLoader.Builder(PlatformContext.INSTANCE).build() }