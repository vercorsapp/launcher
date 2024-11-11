package app.vercors.launcher.core.presentation.ui

import java.text.DecimalFormat

fun Long.countToString(): String = when {
    this < 1_000 -> this.toString()
    this < 1_000_000 -> DecimalFormat("#.#").format(this / 1_000.0) + "k"
    else -> DecimalFormat("#.#").format(this / 1_000_000.0) + "M"
}