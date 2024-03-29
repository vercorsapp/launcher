package app.vercors.menu

import kotlinx.coroutines.flow.StateFlow

interface MenuComponent {
    val uiState: StateFlow<MenuUiState>
    fun onMenuButtonClick(button: MenuButton)
}