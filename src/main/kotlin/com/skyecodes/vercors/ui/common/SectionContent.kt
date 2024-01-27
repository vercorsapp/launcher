package com.skyecodes.vercors.ui.common


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skyecodes.vercors.ui.UI

@Composable
fun SectionContent(title: String, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colors.background).padding(UI.mediumPadding)) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.padding(UI.smallPadding))

        content()
    }
}