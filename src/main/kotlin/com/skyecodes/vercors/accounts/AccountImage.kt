package com.skyecodes.vercors.accounts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import coil3.compose.AsyncImage
import com.skyecodes.vercors.imageRequest
import compose.icons.FeatherIcons
import compose.icons.feathericons.User

@Composable
fun AccountImage(account: Account, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageRequest("account/${account.uuid}", "https://api.mineatar.io/face/${account.uuid}?scale=8"),
        placeholder = rememberVectorPainter(FeatherIcons.User),
        contentDescription = account.name,
        modifier = modifier
    )
}