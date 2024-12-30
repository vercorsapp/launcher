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

import app.vercors.launcher.core.config.mapper.toProto
import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.config.proto.*

sealed class ConfigUpdate<T>(updater: ConfigProtoKt.Dsl.(T) -> Unit) {
    val updater: ConfigProto.(T) -> ConfigProto = { copy { updater(it) } }

    sealed class General<T>(updater: GeneralProtoKt.Dsl.(T) -> Unit) :
        ConfigUpdate<T>({ general = general.copy { updater(it) } }) {
        data object Theme : General<String>({ theme = it })
        data object Accent : General<String>({ accent = it })
        data object Gradient : General<Boolean>({ gradient = it })
        data object Decorated : General<Boolean>({ decorated = it })
        data object Animations : General<Boolean>({ animations = it })
        data object DefaultTab : General<TabConfig>({ defaultTab = it.toProto() })
    }

    sealed class Home<T>(updater: HomeProtoKt.Dsl.(T) -> Unit) : ConfigUpdate<T>({ home = home.copy { updater(it) } }) {
        data object Sections :
            Home<List<HomeSectionConfig>>({ new -> sections.clear(); sections += new.map { it.toProto() } })

        data object Provider : Home<HomeProviderConfig>({ provider = it.toProto() })
    }
}
