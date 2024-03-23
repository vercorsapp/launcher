/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors

import app.vercors.account.AccountDataSource
import app.vercors.account.AccountDataSourceImpl
import app.vercors.account.AccountRepository
import app.vercors.account.AccountRepositoryImpl
import app.vercors.account.auth.AuthenticationDataSource
import app.vercors.account.auth.AuthenticationDataSourceImpl
import app.vercors.account.auth.AuthenticationRepository
import app.vercors.account.auth.AuthenticationRepositoryImpl
import app.vercors.common.AppHttpClient
import app.vercors.common.AppJson
import app.vercors.configuration.ConfigurationDataSource
import app.vercors.configuration.ConfigurationDataSourceImpl
import app.vercors.configuration.ConfigurationRepository
import app.vercors.configuration.ConfigurationRepositoryImpl
import app.vercors.di.DIBuilder
import app.vercors.di.inject
import app.vercors.di.single
import app.vercors.instance.InstanceDataSource
import app.vercors.instance.InstanceDataSourceImpl
import app.vercors.instance.InstanceRepository
import app.vercors.instance.InstanceRepositoryImpl
import app.vercors.instance.mojang.MojangRepository
import app.vercors.instance.mojang.MojangRepositoryImpl
import app.vercors.project.curseforge.CurseforgeProjectDataSource
import app.vercors.project.curseforge.CurseforgeProjectDataSourceImpl
import app.vercors.project.curseforge.CurseforgeProjectRepository
import app.vercors.project.curseforge.CurseforgeProjectRepositoryImpl
import app.vercors.project.modrinth.ModrinthProjectDataSource
import app.vercors.project.modrinth.ModrinthProjectDataSourceImpl
import app.vercors.project.modrinth.ModrinthProjectRepository
import app.vercors.project.modrinth.ModrinthProjectRepositoryImpl
import app.vercors.system.StorageManager
import app.vercors.system.StorageManagerImpl
import ca.gosyer.appdirs.AppDirs
import io.ktor.client.*
import kotlinx.serialization.json.Json
import oshi.SystemInfo
import java.util.*

fun DIBuilder.DataModule(properties: Properties) {
    module {
        single<Json> { AppJson }
        single<HttpClient> { AppHttpClient(inject()) }
        single<AppDirs> { AppDirs(APP_NAME) }
        single<SystemInfo> { SystemInfo() }
        single<ConfigurationRepository> { ConfigurationRepositoryImpl(inject(), inject()) }
        single<ConfigurationDataSource> { ConfigurationDataSourceImpl(inject(), inject()) }
        single<InstanceRepository> { InstanceRepositoryImpl(inject(), inject()) }
        single<InstanceDataSource> { InstanceDataSourceImpl(inject(), inject()) }
        single<AccountRepository> { AccountRepositoryImpl(inject(), inject()) }
        single<AccountDataSource> { AccountDataSourceImpl(inject(), inject()) }
        single<AuthenticationRepository> { AuthenticationRepositoryImpl(inject()) }
        single<AuthenticationDataSource> {
            AuthenticationDataSourceImpl(
                inject(),
                properties.getProperty("microsoftClientId")
            )
        }
        single<AccountDataSource> { AccountDataSourceImpl(inject(), inject()) }
        single<CurseforgeProjectRepository> { CurseforgeProjectRepositoryImpl(inject()) }
        single<CurseforgeProjectDataSource> {
            CurseforgeProjectDataSourceImpl(
                inject(),
                properties.getProperty("curseforgeApiKey")
            )
        }
        single<ModrinthProjectRepository> { ModrinthProjectRepositoryImpl(inject()) }
        single<ModrinthProjectDataSource> {
            ModrinthProjectDataSourceImpl(
                inject(),
                properties.getProperty("modrinthApiKey")
            )
        }
        single<MojangRepository> { MojangRepositoryImpl(inject()) }
        single<StorageManager> { StorageManagerImpl() }
    }
}