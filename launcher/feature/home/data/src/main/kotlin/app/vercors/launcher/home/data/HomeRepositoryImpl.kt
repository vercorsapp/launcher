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

package app.vercors.launcher.home.data

import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.meta.home.HomeApi
import app.vercors.launcher.home.domain.HomeRepository
import app.vercors.launcher.home.domain.HomeSection
import app.vercors.launcher.instance.domain.InstanceRepository
import app.vercors.lib.domain.Resource
import app.vercors.lib.domain.observeResource
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
class HomeRepositoryImpl(
    private val configRepository: ConfigRepository,
    private val instanceRepository: InstanceRepository,
    private val homeApi: HomeApi,
) : HomeRepository {
    override fun observeSections(): Flow<List<HomeSection>> = combineTransform(
        configRepository.observeConfig().map { it.home },
        instanceRepository.observeAll()
    ) { config, instances ->
        val (sectionConfigs, providerConfig) = config
        val (remoteSectionTypes, localSectionTypes) = sectionConfigs.map { it.toType() }.partition { it.remote }
        val localSections = localSectionTypes.map { HomeSection.Instances(it, instances) }
        observeResource {
            homeApi.getHome(
                providerConfig.toType().id,
                remoteSectionTypes.map { it.id })
        }.collect { res ->
            emit(
                localSections + when (res) {
                    Resource.Loading -> remoteSectionTypes.map { HomeSection.Projects(it, Resource.Loading) }
                    is Resource.Success -> res.value.sectionsList.map {
                        HomeSection.Projects(
                            it.type.toSectionType(),
                            Resource.Success(it.projectsList.map { it.toDomain() })
                        )
                    }

                    is Resource.Error -> remoteSectionTypes.map { HomeSection.Projects(it, Resource.Error(res.error)) }
                })
        }
    }
}