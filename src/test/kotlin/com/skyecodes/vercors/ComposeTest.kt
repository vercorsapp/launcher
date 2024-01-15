package com.skyecodes.vercors

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    @Composable
    fun SubPart(state: MutableState<Int>) {
        println("SubPart")
        Button(onClick = { state.value++ }) {
            println("Button")
            Text(state.value.toString())
        }
    }

    application {
        Window(onCloseRequest = ::exitApplication) {
            println("Window")
            val stateA = mutableStateOf(0)
            SubPart(stateA)
        }
    }
}