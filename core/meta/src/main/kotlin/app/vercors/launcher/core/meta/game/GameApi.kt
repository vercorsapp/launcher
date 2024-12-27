package app.vercors.launcher.core.meta.game

import app.vercors.launcher.core.domain.RemoteResult
import app.vercors.meta.game.MetaGameVersionList
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

interface GameApi {
    @GET("v1/game")
    suspend fun getGameVersions(): RemoteResult<MetaGameVersionList>
}

@Single
fun provideGameApi(@Named("metaKtorfit") ktorfit: Ktorfit) = ktorfit.createGameApi()
