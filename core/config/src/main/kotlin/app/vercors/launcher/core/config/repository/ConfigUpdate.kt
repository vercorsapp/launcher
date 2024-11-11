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
