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

package app.vercors.launcher.core.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.*

fun appString(res: Res.string.() -> StringResource): StringResource = Res.string.res()

@Composable
fun appStringResource(vararg formatArgs: String, res: Res.string.() -> StringResource) =
    stringResource(appString(res), *formatArgs)

fun appDrawable(res: Res.drawable.() -> DrawableResource): DrawableResource = Res.drawable.res()

@Composable
fun appPainterResource(res: Res.drawable.() -> DrawableResource) = painterResource(appDrawable(res))

@Composable
fun appImageResource(res: Res.drawable.() -> DrawableResource) = imageResource(appDrawable(res))

@Composable
fun appVectorResource(res: Res.drawable.() -> DrawableResource) = vectorResource(appDrawable(res))

fun appFont(res: Res.font.() -> FontResource): FontResource = Res.font.res()