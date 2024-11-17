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