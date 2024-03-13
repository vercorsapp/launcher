package app.vercors.menu

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.account.AccountData
import app.vercors.account.AccountImage
import app.vercors.common.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.User
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.accounts
import vercors.app.generated.resources.createInstance

@Composable
fun MenuButtonContent(
    button: MenuButton,
    isSelected: Boolean,
    currentAccount: AccountData?,
    onClick: () -> Unit
) {
    when (button) {
        MenuButton.Accounts -> AccountsButton(currentAccount, stringResource(Res.string.accounts), onClick)
        MenuButton.CreateInstance -> NormalMenuButton(
            false,
            stringResource(Res.string.createInstance),
            FeatherIcons.Plus,
            onClick
        )

        MenuButton.Home -> TabMenuButton(isSelected, AppTab.Home, onClick)
        MenuButton.Instances -> TabMenuButton(isSelected, AppTab.Instances, onClick)
        MenuButton.Search -> TabMenuButton(isSelected, AppTab.Search, onClick)
        MenuButton.Settings -> TabMenuButton(isSelected, AppTab.Settings, onClick)
    }
}

@Composable
private fun AccountsButton(currentAccount: AccountData?, title: String, onClick: () -> Unit) {
    currentAccount?.let {
        MenuButtonFrame(title, false, onClick, 0.dp) {
            AccountImage(it, Modifier.fillMaxSize())
        }
    } ?: NormalMenuButton(false, title, FeatherIcons.User, onClick)
}

@Composable
private fun TabMenuButton(isSelected: Boolean, tab: AppTab, onClick: () -> Unit) {
    NormalMenuButton(isSelected, tab.title, tab.icon, onClick)
}

@Composable
private fun NormalMenuButton(isSelected: Boolean, title: String, icon: ImageVector, onClick: () -> Unit) {
    MenuButtonFrame(title, isSelected, onClick) {
        Icon(icon, title, Modifier.fillMaxSize())
    }
}

@Composable
private fun MenuButtonFrame(
    title: String,
    active: Boolean,
    onClick: () -> Unit,
    contentPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = { AppTooltip(title) },
        tooltipPlacement = TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(30.dp, -(25.dp))
        ),
        delayMillis = 250,
    ) {
        val backgroundColor by appAnimateColorAsState(active, MaterialTheme.colors.primary, Color.Transparent)
        val contentColor by appAnimateColorAsState(
            active,
            MaterialTheme.colors.onPrimary,
            MaterialTheme.colors.onSurface
        )

        AppButton(
            modifier = Modifier.size(50.dp),
            elevation = if (active) ButtonDefaults.elevation() else null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(contentPadding),
            shape = UI.largeRoundedCornerShape,
            onClick = onClick
        ) {
            content()
        }
    }
}