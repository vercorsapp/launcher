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

package app.vercors.instance.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.instance.*
import app.vercors.project.icon

@Composable
fun InstanceDetailsContent(
    state: InstanceDetailsState,
    onIntent: (InstanceDetailsIntent) -> Unit
) {
    Card(Modifier.padding(top = UI.largePadding, start = UI.largePadding, end = UI.largePadding)) {
        Row(
            modifier = Modifier.padding(UI.largePadding).fillMaxWidth().height(IntrinsicSize.Max)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
                modifier = Modifier.weight(1f)
            ) {
                InstanceIconContent(
                    instance = state.instance,
                    onLaunch = { onIntent(InstanceDetailsIntent.LaunchInstance) },
                    onStop = { onIntent(InstanceDetailsIntent.StopInstance) },
                    modifier = Modifier.height(120.dp)
                )
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = state.instance.data.name,
                        style = MaterialTheme.typography.h5
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = state.instance.data.loader.icon,
                            contentDescription = null,
                            modifier = Modifier.size(UI.mediumIconSize)
                        )
                        Text(
                            text = state.instance.loaderAndVersionString,
                            fontWeight = FontWeight.Medium,
                            lineHeight = UI.normalLineHeight
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = state.instance.statusIcon,
                            contentDescription = null,
                            modifier = Modifier.size(UI.mediumIconSize),
                            tint = state.instance.statusColor
                        )
                        Text(
                            text = state.instance.statusText,
                            color = state.instance.statusColor,
                            fontWeight = FontWeight.Medium,
                            lineHeight = UI.normalLineHeight
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.instance.status is InstanceStatus.Progress) {
                    val progress by animateFloatAsState((state.instance.status as InstanceStatus.Progress).progress)

                    Text(state.progress?.string ?: "")
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {
                if (!state.instance.data.timePlayed.isZero) {
                    Text(state.instance.timePlayedString)
                }
                if (state.instance.status !is InstanceStatus.Running) {
                    Text(state.instance.lastPlayedString) // TODO make it automatically update
                }
            }
        }
    }
}