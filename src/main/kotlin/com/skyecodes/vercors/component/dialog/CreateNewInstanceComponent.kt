package com.skyecodes.vercors.component.dialog

import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.data.model.mojang.MojangReleaseType
import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import com.skyecodes.vercors.data.service.InstanceService
import com.skyecodes.vercors.data.service.MojangService
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Star
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface CreateNewInstanceComponent {
    val uiState: StateFlow<CreateNewInstanceUiState>
    fun updateInstanceName(instanceName: String)
    fun updateMinecraftVersionDropdown(expanded: Boolean)
    fun updateMinecraftVersion(version: MinecraftVersion)
    fun updateIncludeSnapshots(include: Boolean)
    fun toggleIncludeSnapshots()
    fun updateLoader(loader: Loader?)
    fun updateLoaderVersionDropdown(expanded: Boolean)
    fun close()
    fun createInstance()

    data class CreateNewInstanceUiState(
        val instanceName: String = "",
        val expandedDropdown: Dropdown? = null,
        val minecraftVersion: String = "",
        val includeSnapshots: Boolean = false,
        val minecraftVersions: List<MinecraftVersion> = emptyList(),
        val loader: Loader? = null,
        val loaderVersion: String = ""
    ) {
        val isValid: Boolean =
            instanceName.isNotBlank() && minecraftVersion.isNotEmpty() && (loader == null || loaderVersion.isNotEmpty())

        fun isMinecraftVersionDropdownExpanded() = expandedDropdown === Dropdown.MinecraftVersion
        fun isLoaderVersionDropdownExpanded() = expandedDropdown === Dropdown.LoaderVersion
    }

    enum class Dropdown {
        MinecraftVersion, LoaderVersion
    }

    data class MinecraftVersion(
        val text: String,
        val icon: ImageVector?,
        val isRelease: Boolean,
        val data: MojangVersionManifest.Version
    )
}

class DefaultCreateNewInstanceComponent(
    componentContext: AppComponentContext,
    private val onClose: () -> Unit,
    private val mojangService: MojangService = componentContext.get(),
    private val instanceService: InstanceService = componentContext.get()
) : CreateNewInstanceComponent, AppComponentContext by componentContext {
    override val uiState = MutableStateFlow(CreateNewInstanceComponent.CreateNewInstanceUiState())
    private val instanceName = MutableStateFlow("")
    private val versionManifest: MutableStateFlow<MojangVersionManifest?> = MutableStateFlow(null)
    private val minecraftVersion: MutableStateFlow<CreateNewInstanceComponent.MinecraftVersion?> =
        MutableStateFlow(null)
    private val includeSnapshots = MutableStateFlow(false)
    private val loader: MutableStateFlow<Loader?> = MutableStateFlow(null)

    init {
        doOnCreate { initialize() }
    }

    private fun initializeMinecraftVersion(manifest: MojangVersionManifest) {
        minecraftVersion.value = manifest.convert(manifest.latestRelease)
    }

    private fun initialize() {
        scope.launch {
            val manifest = mojangService.getVersionManifest()
            versionManifest.value = manifest
            initializeMinecraftVersion(manifest)
        }
        scope.launch {
            instanceName.collect { name ->
                uiState.update { it.copy(instanceName = name) }
            }
        }
        scope.launch {
            versionManifest.filterNotNull().collect { manifest ->
                val versions = manifest.versions.map { manifest.convert(it) }
                uiState.update { it.copy(minecraftVersions = versions) }
            }
        }
        scope.launch {
            minecraftVersion.filterNotNull().collect { version ->
                uiState.update { it.copy(minecraftVersion = version.text) }
            }
        }
        scope.launch {
            includeSnapshots.collect { include ->
                uiState.update { it.copy(includeSnapshots = include) }
                if (!include && minecraftVersion.value?.isRelease == false) {
                    versionManifest.value?.let { initializeMinecraftVersion(it) }
                }
            }
        }
        scope.launch {
            loader.collect { l ->
                uiState.update { it.copy(loader = l) }
            }
        }
    }

    private fun MojangVersionManifest.convert(version: MojangVersionManifest.Version): CreateNewInstanceComponent.MinecraftVersion {
        var title = version.id
        var icon: ImageVector? = null
        if (isLatestRelease(version)) {
            title = "Latest release (${version.id})"
            icon = FeatherIcons.Star
        } else if (isLatestSnapshot(version)) {
            title = "Latest snapshot (${version.id})"
            icon = FeatherIcons.Clock
        }
        return CreateNewInstanceComponent.MinecraftVersion(
            title,
            icon,
            version.type === MojangReleaseType.Release,
            version
        )
    }

    override fun updateInstanceName(instanceName: String) {
        this.instanceName.value = instanceName
    }

    override fun updateMinecraftVersionDropdown(expanded: Boolean) =
        updateDropdown(CreateNewInstanceComponent.Dropdown.MinecraftVersion, expanded)

    override fun updateLoaderVersionDropdown(expanded: Boolean) =
        updateDropdown(CreateNewInstanceComponent.Dropdown.LoaderVersion, expanded)

    private fun updateDropdown(dropdown: CreateNewInstanceComponent.Dropdown, expanded: Boolean) {
        uiState.update { it.copy(expandedDropdown = if (expanded) dropdown else null) }
    }

    override fun updateMinecraftVersion(version: CreateNewInstanceComponent.MinecraftVersion) {
        minecraftVersion.value = version
    }

    override fun updateIncludeSnapshots(include: Boolean) {
        includeSnapshots.value = include
    }

    override fun toggleIncludeSnapshots() {
        includeSnapshots.update { !it }
    }

    override fun updateLoader(loader: Loader?) {
        this.loader.value = loader
    }

    override fun close() {
        onClose()
    }

    override fun createInstance() {
        scope.launch {
            instanceService.createInstance(
                instanceName = instanceName.value,
                version = minecraftVersion.value!!.data,
                loader = loader.value,
                loaderVersion = null
            )
        }
        close()
    }
}
