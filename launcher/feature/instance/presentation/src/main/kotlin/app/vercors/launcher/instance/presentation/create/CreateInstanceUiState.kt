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

package app.vercors.launcher.instance.presentation.create

import androidx.compose.runtime.Immutable
import app.vercors.launcher.game.domain.loader.ModLoaderType

@Immutable
data class CreateInstanceUiState(
    val instanceName: String = "",
    val showSnapshots: Boolean = false,
    val selectedGameVersionId: String? = null,
    val gameVersions: List<GameVersionUi>? = null,
    val modLoader: ModLoaderType? = null,
    val modLoaderVersions: List<String>? = null,
    val selectedModLoaderVersion: String? = null,
) {
    val filteredGameVersions: List<GameVersionUi>? =
        if (showSnapshots) gameVersions else gameVersions?.filter { !it.isSnapshot }
    val selectedGameVersion: GameVersionUi? = gameVersions?.find { it.id == selectedGameVersionId }
    val isValid =
        instanceName.isNotBlank() && selectedGameVersionId != null && (modLoader == null || selectedModLoaderVersion != null)
}

data class GameVersionUi(
    val id: String,
    val isSnapshot: Boolean,
    val isLatestRelease: Boolean,
    val isLatestSnapshot: Boolean,
)

operator fun List<GameVersionUi>.get(id: String) = first { it.id == id }
