/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.ui.AppSectionTitle
import app.vercors.launcher.home.presentation.instance.HomeInstancesSectionContent
import app.vercors.launcher.home.presentation.project.HomeProjectsSectionContent
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeSection(
    section: HomeSectionUi,
    onIntent: (HomeUiIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth().heightIn(max = 600.dp)
    ) {
        AppSectionTitle(text = stringResource(section.title))
        when (section) {
            is HomeSectionUi.Instances -> HomeInstancesSectionContent(
                data = section.data,
                onInstanceClick = { onIntent(HomeUiIntent.ShowInstance(it.id)) },
                onInstanceAction = { onIntent(HomeUiIntent.LaunchOrStopInstance(it.id)) },
                onCreateInstance = { onIntent(HomeUiIntent.CreateInstance) }
            )

            is HomeSectionUi.Projects -> HomeProjectsSectionContent(
                data = section.data,
                onProjectClick = { onIntent(HomeUiIntent.ShowProject(it.id)) },
                onProjectAction = { onIntent(HomeUiIntent.InstallProject(it.id)) }
            )
        }
    }
}