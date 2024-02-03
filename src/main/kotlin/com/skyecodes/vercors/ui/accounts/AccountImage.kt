package com.skyecodes.vercors.ui.accounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.skyecodes.vercors.data.model.app.Account
import com.skyecodes.vercors.ui.common.AsyncImage

@Composable
fun AccountImage(account: Account, modifier: Modifier = Modifier) {
    AsyncImage(
        key = "account/${account.uuid}",
        url = "https://api.mineatar.io/face/${account.uuid}?scale=8",
        painterFor = { remember { BitmapPainter(it) } },
        contentDescription = account.name,
        modifier = modifier
    )
}