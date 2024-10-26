package app.vercors.launcher.core.data

import kotlinx.io.files.Path
import java.util.prefs.Preferences

object Storage {
    private var preferences = Preferences.userNodeForPackage(javaClass)
    private var cachedPath: Path? = null

    var path: Path
        get() {
            if (cachedPath == null) {
                cachedPath = Path(preferences.get("path", "."))
            }
            return cachedPath!!
        }
        set(value) {
            cachedPath = value
            preferences.put("path", value.toString())
        }
}