package app.vercors.launcher.core.meta.home

import app.vercors.launcher.core.domain.RemoteResult
import app.vercors.meta.home.MetaHomeSectionList
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

interface HomeApi {
    @GET("v1/home/{provider}")
    suspend fun getHome(@Path provider: String, @Query types: List<String>): RemoteResult<MetaHomeSectionList>
}

@Single
fun provideHomeApi(@Named("metaKtorfit") ktorfit: Ktorfit) = ktorfit.createHomeApi()
