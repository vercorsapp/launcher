package app.vercors.launcher.core.data.storage

import app.vercors.launcher.DummyClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.prefs.Preferences

object Storage {
    private const val PREF_KEY = "path"
    const val DEFAULT_PATH = "."
    private val preferences = Preferences.userNodeForPackage(DummyClass::class.java)
    private val _state = MutableStateFlow(StorageState(preferences.get(PREF_KEY, DEFAULT_PATH)))
    val state: StateFlow<StorageState> = _state.asStateFlow()

    fun updatePath(path: String) {
        preferences.put(PREF_KEY, path)
        _state.update { it.copy(strPath = path) }
    }
}