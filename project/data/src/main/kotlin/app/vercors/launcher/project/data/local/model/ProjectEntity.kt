package app.vercors.launcher.project.data.local.model

import androidx.room.Entity

@Entity(
    tableName = "project",
    primaryKeys = ["provider", "id"]
)
data class ProjectEntity(
    val provider: String,
    val id: String,
)
