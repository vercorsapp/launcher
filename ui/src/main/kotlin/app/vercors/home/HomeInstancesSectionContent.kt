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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.instance.InstanceCardContent
import app.vercors.instance.InstanceData
import kotlin.math.roundToInt

@Composable
fun HomeInstancesSectionContent(
    instances: List<InstanceData>?,
    onInstanceClick: (InstanceData) -> Unit,
    onInstanceLaunchClick: (InstanceData) -> Unit,
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.fillMaxWidth().onGloballyPositioned {
            count = localDensity.run { ((it.size.width.toDp() - 80.dp) / 200.dp).roundToInt() }
        }
    ) {
        if (instances?.isNotEmpty() == true) {
            for (instance in instances.take(count)) {
                InstanceCardContent(
                    instance = instance,
                    onInstanceClick = onInstanceClick,
                    onInstanceLaunchClick = onInstanceLaunchClick,
                    modifier = Modifier.weight(1f)
                )
            }
            for (i in instances.size until count) {
                Spacer(Modifier.weight(1f))
            }
        } else {
            Text("No instances found :(")
        }
    }
}
