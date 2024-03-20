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

package app.vercors.instance

import androidx.compose.runtime.*
import app.vercors.common.ModLoader
import app.vercors.readable
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.lastPlayedTime
import vercors.ui.generated.resources.notPlayedBefore

val Instance.loaderAndVersionString: String @Stable get() = "${data.loader?.value ?: ModLoader.Vanilla} ${data.gameVersion.id}"

val Instance.lastPlayedString: String
    @Composable get() {
        val lastPlayed = data.lastPlayed ?: return stringResource(Res.string.notPlayedBefore)
        var text by remember { mutableStateOf(lastPlayed.readable()) }
        var refresh by remember { mutableStateOf(false) }

        LaunchedEffect(refresh) {
            text = lastPlayed.readable()
            delay(10_000)
            refresh = !refresh
        }

        LaunchedEffect(lastPlayed) {
            text = lastPlayed.readable()
        }

        return stringResource(Res.string.lastPlayedTime, text)
    }