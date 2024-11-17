package app.vercors.launcher.project.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.vercors.launcher.project.domain.ProjectId

@Entity
data class ProjectEntity(
    @PrimaryKey
    @Embedded
    val id: ProjectId
)
