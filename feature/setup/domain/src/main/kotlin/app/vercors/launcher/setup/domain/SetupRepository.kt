package app.vercors.launcher.setup.domain

interface SetupRepository {
    val defaultPath: String
    fun updatePath(path: String)
}