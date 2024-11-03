package app.vercors.launcher.core.storage

import app.vercors.launcher.DummyClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single
import java.util.prefs.Preferences

@Single(createdAtStart = true)
class StorageImpl : Storage {
    private val preferences = Preferences.userNodeForPackage(DummyClass::class.java)
    private val _state = MutableStateFlow(StorageState(strPath = preferences.get(PREF_KEY, Storage.DEFAULT_PATH)))
    override val state: StateFlow<StorageState> = _state.asStateFlow()

    override fun updatePath(path: String) {
        preferences.put(PREF_KEY, path)
        _state.update { it.copy(strPath = path) }
    }

    companion object {
        private const val PREF_KEY = "path"
    }
}