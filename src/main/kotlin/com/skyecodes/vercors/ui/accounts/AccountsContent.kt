package com.skyecodes.vercors.ui.accounts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skyecodes.vercors.component.screen.AccountsComponent
import com.skyecodes.vercors.ui.LocalLocalization

@Composable
fun AccountsContent(component: AccountsComponent) {
    Text(LocalLocalization.current.accounts, Modifier.fillMaxSize())
}