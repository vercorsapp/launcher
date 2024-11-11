package app.vercors.launcher.project.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.vercors.launcher.project.domain.model.ProjectId

@Entity
data class ProjectEntity(
    @PrimaryKey
    @Embedded
    val id: ProjectId
)
