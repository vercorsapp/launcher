package app.vercors.launcher.core.presentation.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State

@Stable
fun <T> immutableStateOf(value: T): State<T> = object : State<T> {
    override var value: T = value
}