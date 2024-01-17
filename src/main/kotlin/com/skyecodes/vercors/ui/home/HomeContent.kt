package com.skyecodes.vercors.ui.home

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.logic.HomeUiState
import com.skyecodes.vercors.logic.HomeViewModel
import com.skyecodes.vercors.ui.UI


@Composable
fun HomeContent(viewModel: HomeViewModel) {
    viewModel.initialize()
    val state by viewModel.uiState.collectAsState()

    Box(Modifier.padding(start = UI.mediumPadding)) {
        val scrollState = rememberScrollState()

        Column(
            Modifier.fillMaxSize()
                .padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.mediumPadding + 6.dp)
                .verticalScroll(scrollState)
        ) {
            state.sections.forEach { (type, section) ->
                when (section) {
                    is HomeUiState.Section.Instances -> HomeInstancesSectionContent(type.title, section.instances)
                    is HomeUiState.Section.Projects -> HomeProjectsSectionContent(type.title, section.projects)
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}