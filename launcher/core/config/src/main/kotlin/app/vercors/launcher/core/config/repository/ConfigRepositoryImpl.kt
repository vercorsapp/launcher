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

package app.vercors.launcher.core.config.repository

import androidx.datastore.core.DataStore
import app.vercors.launcher.core.config.mapper.toConfig
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.ConfigProto
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
class ConfigRepositoryImpl(
    private val dataStore: DataStore<ConfigProto>,
    private val externalScope: CoroutineScope,
) : ConfigRepository {
    private val _configSharedFlow = dataStore.data
        .map { it.toConfig() }
        .onEach { logger.debug { "Configuration updated: $it" } }
        .shareIn(externalScope, SharingStarted.Eagerly, 1)

    override fun observeConfig(): Flow<AppConfig> = _configSharedFlow

    override suspend fun <T> updateConfig(update: ConfigUpdate<T>, value: T) {
        logger.debug { "Updating Config: $update = $value" }
        dataStore.updateData { update.updater(it, value) }
    }
}