package app.vercors.launcher.instance.data.mapper

import app.vercors.launcher.game.data.mapper.ModLoaderTypeMapper
import app.vercors.launcher.instance.data.model.InstanceEntity
import app.vercors.launcher.instance.domain.model.Instance
import app.vercors.launcher.instance.domain.model.InstanceModLoader
import org.koin.core.annotation.Single

@Single
class InstanceEntityMapper(private val modLoaderTypeMapper: ModLoaderTypeMapper) {
    fun fromEntity(entity: InstanceEntity): Instance {
        return Instance(
            id = entity.id,
            name = entity.name,
            gameVersion = entity.gameVersion,
            modLoader = entity.modLoader?.let {
                InstanceModLoader(
                    type = modLoaderTypeMapper.fromData(entity.modLoader),
                    version = entity.modLoaderVersion!!
                )
            },
            createdAt = entity.createdAt,
            lastPlayedAt = entity.lastPlayedAt,
            playTime = entity.playTime,
        )
    }

    fun toEntity(instance: Instance): InstanceEntity {
        return InstanceEntity(
            id = instance.id,
            name = instance.name,
            gameVersion = instance.gameVersion,
            modLoader = instance.modLoader?.type?.let { modLoaderTypeMapper.toData(it) },
            modLoaderVersion = instance.modLoader?.version,
            createdAt = instance.createdAt,
            lastPlayedAt = instance.lastPlayedAt,
            playTime = instance.playTime,
        )
    }
}