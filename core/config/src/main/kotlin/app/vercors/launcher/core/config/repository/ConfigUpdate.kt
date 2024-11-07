package app.vercors.launcher.core.config.repository

import app.vercors.launcher.core.config.mapper.toProto
import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.config.proto.*

typealias ConfigUpdate<T> = ConfigProto.(T) -> ConfigProto

private fun <T> configUpdate(updater: ConfigProtoKt.Dsl.(T) -> Unit): ConfigUpdate<T> = { copy { updater(it) } }

val ThemeConfigUpdate: ConfigUpdate<String> = generalConfigUpdate { theme = it }
val AccentConfigUpdate: ConfigUpdate<String> = generalConfigUpdate { accent = it }
val DecoratedConfigUpdate: ConfigUpdate<Boolean> = generalConfigUpdate { decorated = it }
val AnimationsConfigUpdate: ConfigUpdate<Boolean> = generalConfigUpdate { animations = it }
val DefaultTabConfigUpdate: ConfigUpdate<TabConfig> = generalConfigUpdate { defaultTab = it.toProto() }

private fun <T> generalConfigUpdate(updater: GeneralProtoKt.Dsl.(T) -> Unit): ConfigUpdate<T> =
    configUpdate { general = general.copy { updater(it) } }

val HomeSectionConfigUpdate: ConfigUpdate<List<HomeSectionConfig>> =
    homeConfigUpdate { new -> sections.clear(); sections += new.map { it.toProto() } }
val HomeProviderConfigUpdate: ConfigUpdate<HomeProviderConfig> = homeConfigUpdate { provider = it.toProto() }

private fun <T> homeConfigUpdate(updater: HomeProtoKt.Dsl.(T) -> Unit): ConfigUpdate<T> =
    configUpdate { home = home.copy { updater(it) } }