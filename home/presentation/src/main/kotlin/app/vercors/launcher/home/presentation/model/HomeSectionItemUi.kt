package app.vercors.launcher.home.presentation.model

sealed interface HomeSectionItemUi {
    data class Instance(
        val name: String,
        val loader: String,
        val gameVersion: String,
        val status: HomeInstanceStatusUi
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

