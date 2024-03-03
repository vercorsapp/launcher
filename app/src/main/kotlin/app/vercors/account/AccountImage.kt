package app.vercors.account

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import app.vercors.common.AppAsyncImage
import app.vercors.imageRequest
import compose.icons.FeatherIcons
import compose.icons.feathericons.User

@Composable
fun AccountImage(
    accountData: AccountData,
    modifier: Modifier = Modifier
) {
    AppAsyncImage(
        model = imageRequest("account/${accountData.uuid}", "https://api.mineatar.io/face/${accountData.uuid}?scale=8"),
        placeholder = rememberVectorPainter(FeatherIcons.User),
        contentDescription = accountData.name,
        modifier = modifier
    )
}