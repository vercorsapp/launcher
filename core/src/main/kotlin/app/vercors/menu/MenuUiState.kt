package app.vercors.menu

import app.vercors.account.AccountData
import app.vercors.common.AppTab
import kotlinx.collections.immutable.ImmutableList

data class MenuUiState(
    val topButtons: ImmutableList<MenuButton>,
    val bottomButtons: ImmutableList<MenuButton>,
    val selectedTab: AppTab,
    val selectedAccount: AccountData?,
)
