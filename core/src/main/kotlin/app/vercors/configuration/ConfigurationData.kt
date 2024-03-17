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

import app.vercors.common.AppColor
import app.vercors.common.AppDarkTheme
import app.vercors.common.AppTab
import app.vercors.common.AppTheme
import app.vercors.home.HomeSectionType
import app.vercors.instance.InstanceSorter
import app.vercors.project.ProjectProviderType
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationData(
    val version: Int = 0,
    val showTutorial: Boolean = false,
    val theme: AppTheme = AppTheme.System,
    val darkTheme: AppDarkTheme = AppDarkTheme.Normal,
    val accentColor: AppColor = AppColor.Mauve,
    val useSystemWindowFrame: Boolean = false,
    val animations: Boolean = true,
    val defaultTab: AppTab = AppTab.Home,
    val homeSections: List<HomeSectionType> = HomeSectionType.entries,
    val homeProviders: List<ProjectProviderType> = ProjectProviderType.entries,
    val savedInstanceSorter: InstanceSorter = InstanceSorter(),
    val maximumParallelThreads: Int = Runtime.getRuntime().availableProcessors().coerceAtMost(8),
    val java8Path: String? = null,
    val java17Path: String? = null,
    val defaultAllocatedRam: Int? = null,
    val jvmArguments: String = ""
) {
    companion object {
        val DEFAULT = ConfigurationData()
    }
}