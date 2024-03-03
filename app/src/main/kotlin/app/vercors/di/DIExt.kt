package app.vercors.di

import androidx.compose.runtime.Composable
import app.vercors.LocalDI

@Composable
inline fun <reified T : Any> inject(vararg args: Any): T = LocalDI.current.inject(*args)