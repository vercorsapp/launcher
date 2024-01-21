package com.skyecodes.vercors.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skyecodes.vercors.component.screen.SearchComponent
import com.skyecodes.vercors.ui.LocalLocalization


@Composable
fun SearchContent(component: SearchComponent) {
    Text(LocalLocalization.current.search, Modifier.fillMaxSize())
}
