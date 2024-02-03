package com.skyecodes.vercors.ui.accounts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.skyecodes.vercors.applyIf
import com.skyecodes.vercors.data.model.app.Account
import com.skyecodes.vercors.data.service.AccountData
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.AppButton
import com.skyecodes.vercors.ui.common.AppTooltip
import compose.icons.FeatherIcons
import compose.icons.feathericons.UserPlus
import compose.icons.feathericons.UserX

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountsPopup(
    accountData: AccountData.Loaded,
    onTogglePopup: () -> Unit,
    onSelectAccount: (Account) -> Unit,
    onRemoveAccount: (Account) -> Unit,
    onAddAccount: () -> Unit
) {
    val locale = LocalLocalization.current
    val colors = MaterialTheme.colors
    Popup(
        alignment = Alignment.BottomStart,
        offset = IntOffset.Zero,
        onDismissRequest = onTogglePopup,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier.defaultMinSize(minWidth = 350.dp).width(IntrinsicSize.Max)
                .padding(start = UI.mediumPadding, bottom = UI.largePadding).shadow(8.dp)
        ) {
            Column {
                accountData.accounts.forEach {
                    val isSelected = it.uuid == accountData.selected
                    AccountsPopupEntry(
                        clickable = !isSelected,
                        onClick = { onSelectAccount(it) },
                        modifier = if (isSelected) Modifier.background(colors.primary) else Modifier
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
                                        append(" (${locale.selected})")
                                    }
                                },
                                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                            )
                        }

                        TooltipArea(
                            tooltip = { AppTooltip(locale.logOut) },
                            tooltipPlacement = TooltipPlacement.ComponentRect(
                                alignment = Alignment.CenterEnd,
                                offset = DpOffset(25.dp, -(20.dp))
                            )
                        ) {
                            AppButton(
                                modifier = Modifier.size(40.dp),
                                onClick = { onRemoveAccount(it) },
                                colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                                contentPadding = PaddingValues(UI.mediumPadding)
                            ) {
                                Icon(FeatherIcons.UserX, locale.logOut, Modifier.fillMaxSize())
                            }
                        }
                    }
                }
                AccountsPopupEntry(true, onAddAccount) {
                    Icon(FeatherIcons.UserPlus, null, Modifier.padding(UI.smallPadding).fillMaxHeight().aspectRatio(1f))
                    Text(locale.addAccount)
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