package app.vercors.launcher.account.data.mapper

import app.vercors.launcher.account.data.model.AccountEntity
import app.vercors.launcher.account.domain.model.Account
import org.koin.core.annotation.Single

@Single
class AccountEntityMapper {
    fun fromData(data: AccountEntity): Account = Account(
        uuid = data.uuid,
        username = data.username,
        accessToken = data.accessToken,
        refreshToken = data.refreshToken
    )

    fun toData(account: Account): AccountEntity = AccountEntity(
        uuid = account.uuid,
        username = account.username,
        accessToken = account.accessToken,
        refreshToken = account.refreshToken
    )
}