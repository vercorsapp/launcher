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

package app.vercors.configuration

import app.vercors.home.HomeSectionType
import app.vercors.project.ProjectProviderType

sealed interface ConfigurationIntent {
    @JvmInline
    value class UpdateConfig(val update: (ConfigurationData) -> ConfigurationData) : ConfigurationIntent

    @JvmInline
    value class UpdateHomeSection(val section: HomeSectionType) : ConfigurationIntent

    @JvmInline
    value class UpdateHomeProvider(val provider: ProjectProviderType) : ConfigurationIntent
    data class OpenDirectoryPicker(
        val initialPath: String?,
        val onSelectPath: ConfigurationData.(String) -> ConfigurationData
    ) : ConfigurationIntent

    data object CloseDirectoryPicker : ConfigurationIntent
    data object ToggleCustomMemory : ConfigurationIntent
    data class UpdateCustomMemory(val value: Int, val fromSlider: Boolean) : ConfigurationIntent
}
