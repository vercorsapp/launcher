package com.skyecodes.snowball.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.skyecodes.snowball.ui.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme(colors = Theme.Colors.mocha, typography = Typography(Theme.Font.family)) {
        Surface(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.background)) {
            Navigator(
                screen = HomeScreen
            )
        }
    }
}