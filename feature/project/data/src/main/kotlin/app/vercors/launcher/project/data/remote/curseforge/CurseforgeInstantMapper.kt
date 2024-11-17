package app.vercors.launcher.project.data.remote.curseforge

import app.vercors.launcher.project.data.remote.curseforge.dto.CurseforgeInstant
import kotlinx.datetime.Instant

fun CurseforgeInstant.toInstant(): Instant = this