package app.vercors.launcher.project.data.remote.modrinth

import app.vercors.launcher.project.data.remote.modrinth.dto.ModrinthInstant
import kotlinx.datetime.Instant

fun ModrinthInstant.toInstant(): Instant = this