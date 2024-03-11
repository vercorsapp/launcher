package app.vercors.common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

val AppColor.title: String
    @Composable get() = stringResource(
        when (this) {
            AppColor.Rosewater -> Res.string.rosewater
            AppColor.Flamingo -> Res.string.flamingo
            AppColor.Pink -> Res.string.pink
            AppColor.Mauve -> Res.string.mauve
            AppColor.Red -> Res.string.red
            AppColor.Maroon -> Res.string.maroon
            AppColor.Peach -> Res.string.peach
            AppColor.Yellow -> Res.string.yellow
            AppColor.Green -> Res.string.green
            AppColor.Teal -> Res.string.teal
            AppColor.Sky -> Res.string.sky
            AppColor.Sapphire -> Res.string.sapphire
            AppColor.Blue -> Res.string.blue
            AppColor.Lavender -> Res.string.lavender
        }
    )