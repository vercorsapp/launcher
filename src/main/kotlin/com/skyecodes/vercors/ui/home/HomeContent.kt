package com.skyecodes.vercors.ui.home

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.component.screen.HomeComponent
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.SectionContent


@Composable
fun HomeContent(component: HomeComponent) {
    val state by component.uiState.collectAsState()

    Box(Modifier.padding(start = UI.mediumPadding)) {
        val scrollState = rememberScrollState()

        Column(
            Modifier.fillMaxSize()
                .padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.mediumPadding + 6.dp)
                .verticalScroll(scrollState)
        ) {
            state.sections.forEach { (type, section) ->
                SectionContent(type.localizedTitle(LocalLocalization.current)) {
                    when (section) {
                        is HomeComponent.UiState.Section.Instances -> HomeInstancesSectionContent(
                            section.instances,
                            component.showInstanceDetails,
                            component.launchInstance
                        )

                        is HomeComponent.UiState.Section.Projects -> HomeProjectsSectionContent(
                            section.projects,
                            component.showProjectDetails,
                            component.installProject
                        )
                    }
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}


@Composable
fun FakeHomeCard(box: @Composable (Modifier) -> Unit) {
    val colors = listOf(LocalPalette.current.surface1, LocalPalette.current.surface0)
    val limit = 1.5f
    val transition = rememberInfiniteTransition()
    val progressAnimated by transition.animateFloat(
        initialValue = -limit,
        targetValue = limit,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    box(Modifier.drawWithCache {
        val width = size.width
        val height = size.height

        val offset = width * progressAnimated

        val brush = Brush.linearGradient(
            colors = colors,
            start = Offset(offset, 0f),
            end = Offset(offset + width, height)
        )

        onDrawWithContent {
            drawRoundRect(
                brush = brush,
                blendMode = BlendMode.SrcIn,
                cornerRadius = CornerRadius(5f)
            )
        }
    })
}