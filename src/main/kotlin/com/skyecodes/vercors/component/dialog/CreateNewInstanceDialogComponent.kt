package com.skyecodes.vercors.component.dialog

import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.data.model.mojang.MojangReleaseType
import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import com.skyecodes.vercors.data.service.MojangService
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Star
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface CreateNewInstanceDialogComponent {
    val uiState: StateFlow<CreateNewInstanceUiState>
    fun updateInstanceName(instanceName: String)
    fun updateMinecraftVersion(version: MinecraftVersion)
    fun updateIncludeSnapshots(include: Boolean)
    fun toggleIncludeSnapshots()
    fun updateLoader(loader: Loader?)
    fun close()
    fun createInstance()

    data class CreateNewInstanceUiState(
        val instanceName: String = "",
        val minecraftVersion: String = "",
        val includeSnapshots: Boolean = false,
        val minecraftVersions: List<MinecraftVersion> = emptyList(),
        val loader: Loader? = null,
        val loaderVersion: String = "",
        val isValid: Boolean = false
    )

    data class MinecraftVersion(
        val text: String,
        val icon: ImageVector?,
        val isRelease: Boolean,
        val data: MojangVersionManifest.Version
    )
}

class DefaultCreateNewInstanceDialogComponent(
    componentContext: AppComponentContext,
    private val onCreateInstance: (Instance) -> Unit,
    private val onClose: () -> Unit,
    private val mojangService: MojangService = componentContext.get(),
) : CreateNewInstanceDialogComponent, AppComponentContext by componentContext {
    override val uiState = MutableStateFlow(CreateNewInstanceDialogComponent.CreateNewInstanceUiState())
    private val instanceName = MutableStateFlow("")
    private val versionManifest: MutableStateFlow<MojangVersionManifest?> = MutableStateFlow(null)
    private val minecraftVersion: MutableStateFlow<CreateNewInstanceDialogComponent.MinecraftVersion?> =
        MutableStateFlow(null)
    private val includeSnapshots = MutableStateFlow(false)
    private val loader: MutableStateFlow<Loader?> = MutableStateFlow(null)
    private val loaderVersion: MutableStateFlow<String?> = MutableStateFlow(null)

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
                updateUi { it.copy(instanceName = name) }
            }
        }
        scope.launch {
            versionManifest.filterNotNull().collect { manifest ->
                updateMinecraftVersions(manifest, includeSnapshots.value)
            }
        }
        scope.launch {
            minecraftVersion.filterNotNull().collect { version ->
                updateUi { it.copy(minecraftVersion = version.text) }
            }
        }
        scope.launch {
            includeSnapshots.collect { include ->
                updateUi { it.copy(includeSnapshots = include) }
                versionManifest.value?.let { updateMinecraftVersions(it, include) }
            }
        }
        scope.launch {
            loader.collect { l ->
                updateUi { it.copy(loader = l) }
            }
        }
    }

    private fun updateUi(state: (CreateNewInstanceDialogComponent.CreateNewInstanceUiState) -> CreateNewInstanceDialogComponent.CreateNewInstanceUiState) {
        uiState.update { state(it).copy(isValid = checkValidity()) }
    }

    private fun checkValidity(): Boolean =
        instanceName.value.filter { it.isLetterOrDigit() }.isNotBlank()
                && minecraftVersion.value != null
                && (loader.value == null || !loaderVersion.value.isNullOrBlank())

    private fun MojangVersionManifest.convert(version: MojangVersionManifest.Version): CreateNewInstanceDialogComponent.MinecraftVersion {
        var title = version.id
        var icon: ImageVector? = null
        if (isLatestRelease(version)) {
            title = "Latest release (${version.id})"
            icon = FeatherIcons.Star
        } else if (isLatestSnapshot(version)) {
            title = "Latest snapshot (${version.id})"
            icon = FeatherIcons.Clock
        }
        return CreateNewInstanceDialogComponent.MinecraftVersion(
            title,
            icon,
            version.type === MojangReleaseType.Release,
            version
        )
    }

    private fun updateMinecraftVersions(manifest: MojangVersionManifest, includeSnapshots: Boolean) {
        val versions = manifest.versions
            .map { manifest.convert(it) }
            .filter { version -> version.isRelease || includeSnapshots }
        updateUi { it.copy(minecraftVersions = versions) }
        if (!includeSnapshots && minecraftVersion.value?.isRelease == false) {
            initializeMinecraftVersion(manifest)
        }
    }

    override fun updateInstanceName(instanceName: String) {
        this.instanceName.value = instanceName
    }

    override fun updateMinecraftVersion(version: CreateNewInstanceDialogComponent.MinecraftVersion) {
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
        onCreateInstance(
            Instance(
                name = instanceName.value.trim(),
                gameVersion = minecraftVersion.value!!.data,
                loader = loader.value,
                loaderVersion = null
            )
        )
        close()
    }
}
