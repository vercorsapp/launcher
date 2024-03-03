package app.vercors.home

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
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.common.SectionContent

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
            state.sections.forEach {
                SectionContent(it.type.title) {
                    when (it) {
                        is HomeSection.Instances -> HomeInstancesSectionContent(
                            it.instances,
                            component.onShowInstanceDetails,
                            component.onLaunchInstance
                        )

                        is HomeSection.Projects -> HomeProjectsSectionContent(
                            it.projects,
                            component.onShowProjectDetails,
                            component.onInstallProject
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