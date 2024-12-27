package app.vercors.launcher.core.meta

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.protobuf.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.annotation.*

@Module
@ComponentScan
class CoreMetaModule

@OptIn(ExperimentalSerializationApi::class)
@Single
@Named("metaKtorfit")
internal fun provideMetaKtorfit(
    httpClient: HttpClient,
    @Property("vercorsApiKey") apiKey: String,
    @Property("vercorsApiUrl") apiUrl: String
): Ktorfit = Ktorfit.Builder()
    .httpClient(httpClient.config {
        install(DefaultRequest) {
            bearerAuth(apiKey)
        }
        install(ContentNegotiation) {
            protobuf()
        }
    })
    .converterFactories(RemoteResultConverterFactory())
    .baseUrl(apiUrl.let { if (it.endsWith("/")) it else "$it/" })
    .build()