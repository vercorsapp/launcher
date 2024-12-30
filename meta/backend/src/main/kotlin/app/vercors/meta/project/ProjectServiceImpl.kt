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

package app.vercors.meta.project

import app.vercors.meta.project.curseforge.CurseforgeService
import app.vercors.meta.project.modrinth.ModrinthService
import org.koin.core.annotation.Single

@Single
class ProjectServiceImpl(
    private val modrinthService: ModrinthService,
    private val curseforgeService: CurseforgeService
) : ProjectService {
    override suspend fun searchProject(
        provider: MetaProjectProvider,
        type: MetaProjectType,
        limit: Int
    ): List<MetaProject> = getProviderService(provider).search(type, limit)

    private fun getProviderService(provider: MetaProjectProvider): ProviderService = when (provider) {
        MetaProjectProvider.modrinth -> modrinthService
        MetaProjectProvider.curseforge -> curseforgeService
        MetaProjectProvider.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized project provider")
    }
}