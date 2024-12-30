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

package app.vercors.launcher.home.presentation.instance

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.vercors.launcher.home.presentation.HomeLoadingCard
import app.vercors.launcher.home.presentation.HomeSectionDataUi
import app.vercors.launcher.home.presentation.HomeSectionItemUi

val HomeInstanceCardWidth = 220.dp

@Composable
fun HomeInstancesSectionContent(
    data: HomeSectionDataUi<HomeSectionItemUi.Instance>,
    onInstanceClick: (HomeSectionItemUi.Instance) -> Unit,
    onInstanceAction: (HomeSectionItemUi.Instance) -> Unit,
    onCreateInstance: () -> Unit,
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val density = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).onGloballyPositioned {
            count = with(density) { (it.size.width.toDp() / HomeInstanceCardWidth).toInt() }
        }
    ) {
        when (data) {
            is HomeSectionDataUi.Loaded -> {
                for (instance in data.items.take(count)) {
                    HomeInstanceCard(
                        instance = instance,
                        onInstanceClick = { onInstanceClick(instance) },
                        onInstanceAction = { onInstanceAction(instance) }
                    )
                }
                if (data.items.size < count) {
                    HomeCreateInstanceCard(
                        onClick = onCreateInstance
                    )
                }
                for (i in data.items.size + 1 until count) {
                    Spacer(Modifier.weight(1f))
                }
            }

            is HomeSectionDataUi.Loading -> {
                repeat(count) {
                    HomeLoadingCard { HomeInstanceCardBox(it) }
                }
            }
        }
    }
}