package app.vercors.launcher.setup.domain.repository

interface SetupRepository {
    val defaultPath: String
    fun updatePath(path: String)
}