package app.vercors.launcher.project.data.remote.modrinth.mapper

import app.vercors.launcher.project.data.remote.modrinth.model.ModrinthInstant
import kotlinx.datetime.Instant

fun ModrinthInstant.toInstant(): Instant = this