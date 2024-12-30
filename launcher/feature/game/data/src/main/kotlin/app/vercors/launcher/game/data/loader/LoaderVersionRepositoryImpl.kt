/*
 * Copyright (c) 2024 skyecodes
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

package app.vercors.launcher.game.data.loader

import app.vercors.launcher.core.domain.DomainError
import app.vercors.launcher.core.domain.Resource
import app.vercors.launcher.core.domain.map
import app.vercors.launcher.core.domain.observeResource
import app.vercors.launcher.core.meta.loader.LoaderApi
import app.vercors.launcher.game.data.toStringData
import app.vercors.launcher.game.domain.loader.LoaderVersion
import app.vercors.launcher.game.domain.loader.LoaderVersionRepository
import app.vercors.launcher.game.domain.loader.ModLoaderType
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LoaderVersionRepositoryImpl(
    val loaderApi: LoaderApi
) : LoaderVersionRepository {
    override fun observeLoaderVersions(
        loader: ModLoaderType,
        gameVersion: String
    ): Flow<Resource<List<LoaderVersion>, DomainError>> = observeResource {
        loaderApi.getLoaderVersions(loader.toStringData(), gameVersion).map { it.toLoaderVersionList() }
    }
}