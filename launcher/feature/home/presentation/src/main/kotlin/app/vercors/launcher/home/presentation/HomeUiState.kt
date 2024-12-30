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

package app.vercors.launcher.home.presentation

import androidx.compose.runtime.Immutable
import app.vercors.launcher.instance.domain.InstanceId
import app.vercors.launcher.project.domain.ProjectId
import org.jetbrains.compose.resources.StringResource
import java.time.Instant

@JvmInline
@Immutable
value class HomeUiState(
    val sections: List<HomeSectionUi> = emptyList(),
)

sealed interface HomeSectionUi {
    val title: StringResource

    data class Instances(override val title: StringResource, val data: HomeSectionDataUi<HomeSectionItemUi.Instance>) :
        HomeSectionUi

    data class Projects(override val title: StringResource, val data: HomeSectionDataUi<HomeSectionItemUi.Project>) :
        HomeSectionUi
}

sealed interface HomeSectionDataUi<T> {
    class Loading<T> : HomeSectionDataUi<T>

    @JvmInline
    value class Loaded<T>(val items: List<T>) : HomeSectionDataUi<T>
}

sealed interface HomeSectionItemUi {
    data class Instance(
        val id: InstanceId,
        val name: String,
        val loader: String,
        val gameVersion: String,
        val status: HomeInstanceStatusUi
    ) : HomeSectionItemUi {
        val loaderAndGameVersion = "$loader $gameVersion"
    }

    data class Project(
        val id: ProjectId,
        val name: String,
        val author: String,
        val iconUrl: String?,
        val imageUrl: String?,
        val description: String,
        val downloadCount: String,
        val lastUpdated: Instant
    ) : HomeSectionItemUi

}

sealed interface HomeInstanceStatusUi {
    @JvmInline
    value class NotRunning(val lastPlayed: String) : HomeInstanceStatusUi
    data object Running : HomeInstanceStatusUi
}
