package app.vercors.launcher.account.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey
    val uuid: String,
    val username: String,
    val accessToken: String?,
    val refreshToken: String?,
)
