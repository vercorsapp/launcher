/*
 * Copyright (c) 2024-2025 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.core.meta.loader

import app.vercors.launcher.core.domain.RemoteResult
import app.vercors.meta.loader.MetaLoaderVersionList
import app.vercors.meta.loader.MetaLoaderVersionMap
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

interface LoaderApi {
    @GET("v1/loader/all/{gameVersion}")
    suspend fun getAllLoadersVersions(@Path gameVersion: String): RemoteResult<MetaLoaderVersionMap>
    @GET("v1/loader/{loader}/{gameVersion}")
    suspend fun getLoaderVersions(@Path loader: String, @Path gameVersion: String): RemoteResult<MetaLoaderVersionList>
}

@Single
fun provideLoaderApi(@Named("metaKtorfit") ktorfit: Ktorfit) = ktorfit.createLoaderApi()
