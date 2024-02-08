package com.skyecodes.vercors.projects

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skyecodes.vercors.common.LocalLocalization


@Composable
fun SearchContent(component: SearchComponent) {
    Text(LocalLocalization.current.search, Modifier.fillMaxSize())
}
