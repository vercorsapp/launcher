package app.vercors.launcher.instance.data

import app.vercors.launcher.game.data.toModLoaderType
import app.vercors.launcher.game.data.toStringData
import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.instance.domain.InstanceModLoader

fun InstanceEntity.toInstance(): Instance {
    return Instance(
        id = id,
        name = name,
        gameVersion = gameVersion,
        modLoader = modLoader?.let {
            InstanceModLoader(
                type = modLoader.toModLoaderType(),
                version = modLoaderVersion!!
            )
        },
        createdAt = createdAt,
        lastPlayedAt = lastPlayedAt,
        playTime = playTime,
    )
}

fun Instance.toEntity(): InstanceEntity {
    return InstanceEntity(
        id = id,
        name = name,
        gameVersion = gameVersion,
        modLoader = modLoader?.type?.toStringData(),
        modLoaderVersion = modLoader?.version,
        createdAt = createdAt,
        lastPlayedAt = lastPlayedAt,
        playTime = playTime,
    )
}