package app.vercors.launcher.account.data

import app.vercors.launcher.account.domain.Account

fun AccountEntity.toAccount(): Account = Account(
    uuid = uuid,
    username = username,
    accessToken = accessToken,
    refreshToken = refreshToken
)

fun Account.toEntity(): AccountEntity = AccountEntity(
    uuid = uuid,
    username = username,
    accessToken = accessToken,
    refreshToken = refreshToken
)