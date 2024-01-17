package com.skyecodes.vercors.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.ui.common.SectionContent

@Composable
fun HomeInstancesSectionContent(title: String, instances: List<Instance>?) {
    if (instances == null) {
        Text("Loading...")
    } else if (instances.isNotEmpty()) {
        SectionContent(title) {
            Row {
                instances.forEach { Text(it.name) }
            }
        }
    }
}