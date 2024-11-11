package app.vercors.launcher.project.data.remote.curseforge.mapper

import app.vercors.launcher.project.data.remote.curseforge.model.CurseforgeInstant
import kotlinx.datetime.Instant

fun CurseforgeInstant.toInstant(): Instant = this