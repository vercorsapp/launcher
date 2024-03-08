package app.vercors.menu

import app.vercors.account.AccountData
import app.vercors.common.AppTab

data class MenuUiState(
    val topButtons: List<MenuButton>,
    val bottomButtons: List<MenuButton>,
    val selectedTab: AppTab,
    val selectedAccount: AccountData?,
)
