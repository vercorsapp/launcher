package app.vercors.launcher.core.meta.loader

import app.vercors.launcher.core.domain.RemoteResult
import app.vercors.meta.loader.MetaLoaderVersionList
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

interface LoaderApi {
    @GET("v1/loader/{loader}/{gameVersion}")
    suspend fun getLoaderVersions(@Path loader: String, @Path gameVersion: String): RemoteResult<MetaLoaderVersionList>
}

@Single
fun provideLoaderApi(@Named("metaKtorfit") ktorfit: Ktorfit) = ktorfit.createLoaderApi()
