package app.vercors.account

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.applyIf
import app.vercors.common.AppButton
import app.vercors.common.AppTooltip
import app.vercors.common.appAnimateColorAsState
import app.vercors.common.appAnimateContentSize
import compose.icons.FeatherIcons
import compose.icons.feathericons.LogOut
import compose.icons.feathericons.UserPlus
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.logIn
import vercors.app.generated.resources.logOut
import vercors.app.generated.resources.selected

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun AccountListContent(component: AccountListComponent) {
    val uiState by component.uiState.collectAsState()

    if (uiState.isPopupOpen) {
        Popup(
            alignment = Alignment.BottomStart,
            offset = IntOffset.Zero,
            onDismissRequest = component::onTogglePopup,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier.defaultMinSize(minWidth = 350.dp).width(IntrinsicSize.Max)
                    .padding(start = UI.mediumPadding, bottom = UI.largePadding).shadow(8.dp)
                    .appAnimateContentSize()
            ) {
                Column {
                    uiState.data.accounts.forEach {
                        val isSelected = it.uuid == uiState.data.selected
                        val backgroundColor by appAnimateColorAsState(if (isSelected) MaterialTheme.colors.primary else Color.Transparent)
                        val contentColor by appAnimateColorAsState(if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface)

                        AccountsPopupEntry(
                            clickable = !isSelected,
                            onClick = { component.onSelectAccount(it) },
                            modifier = Modifier.background(backgroundColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(UI.smallPadding).weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AccountImage(it, Modifier.fillMaxHeight().padding(end = UI.smallPadding))
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = MaterialTheme.typography.h6.toSpanStyle()) {
                                            append(it.name)
                                        }
                                        if (isSelected) {
                                            append(" (${stringResource(Res.string.selected)})")
                                        }
                                    },
                                    color = contentColor
                                )
                            }

                            TooltipArea(
                                tooltip = { AppTooltip(stringResource(Res.string.logOut)) },
                                tooltipPlacement = TooltipPlacement.ComponentRect(
                                    alignment = Alignment.CenterEnd,
                                    offset = DpOffset(25.dp, -(20.dp))
                                )
                            ) {
                                AppButton(
                                    modifier = Modifier.size(40.dp),
                                    onClick = { component.onRemoveAccount(it) },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                                    contentPadding = PaddingValues(UI.mediumPadding)
                                ) {
                                    Icon(FeatherIcons.LogOut, stringResource(Res.string.logOut), Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                    AccountsPopupEntry(true, component::onAddAccount) {
                        Icon(
                            FeatherIcons.UserPlus,
                            null,
                            Modifier.padding(UI.smallPadding).fillMaxHeight().aspectRatio(1f)
                        )
                        Text(stringResource(Res.string.logIn))
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountsPopupEntry(
    clickable: Boolean,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) =
    Row(
        modifier = modifier.height(60.dp).fillMaxWidth()
            .applyIf(clickable) { clickable(onClick = onClick).pointerHoverIcon(PointerIcon.Hand) }
            .padding(UI.mediumPadding),
        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )