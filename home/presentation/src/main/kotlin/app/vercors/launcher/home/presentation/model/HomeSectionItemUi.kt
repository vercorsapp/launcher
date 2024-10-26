package app.vercors.launcher.home.presentation.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

sealed interface HomeSectionItemUi {
    data class Instance(
        val name: String,
        val loader: String,
        val gameVersion: String,
        val status: String,
        val statusIcon: DrawableResource,
        val statusColor: Color
    ) : HomeSectionItemUi {
        val loaderAndGameVersion = "$loader $gameVersion"
    }

    data class Project(
        val name: String,
        val author: String,
        val iconUrl: String,
        val imageUrl: String,
        val description: String,
        val downloadCount: String,
        val lastUpdated: String
    ) : HomeSectionItemUi
}