/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    Box(Modifier.padding(start = UI.mediumPadding)) {
        val scrollState = rememberScrollState()

        Column(
            Modifier.fillMaxSize()
                .padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.mediumPadding + 6.dp)
                .verticalScroll(scrollState)
        ) {
            state.sections.forEach { section ->
                SectionContent(section.type.title) {
                    when (section) {
                        is HomeSection.Instances -> HomeInstancesSectionContent(
                            instances = section.instances,
                            onInstanceClick = { onIntent(HomeIntent.ShowInstanceDetails(it)) },
                            onInstanceLaunchClick = { onIntent(HomeIntent.LaunchInstance(it)) }
                        )

                        is HomeSection.Projects -> HomeProjectsSectionContent(
                            projects = section.projects,
                            onProjectClick = { onIntent(HomeIntent.ShowProjectDetails(it)) },
                            onProjectInstallClick = { onIntent(HomeIntent.InstallProject(it)) }
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