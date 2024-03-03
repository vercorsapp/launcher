package app.vercors.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuContent(component: MenuComponent) {
    val uiState by component.uiState.collectAsState()

    @Composable
    fun menuButtons(buttons: List<MenuButton>) {
        buttons.forEach {
            MenuButtonContent(
                it,
                uiState.selectedTab === it.tab,
                uiState.selectedAccount
            ) { component.onMenuButtonClick(it) }
        }
    }

    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        menuButtons(uiState.topButtons)
        Spacer(Modifier.weight(1f))
        menuButtons(uiState.bottomButtons)
    }
}
