package app.vercors.launcher.game

import app.vercors.launcher.game.data.GameDataModule
import app.vercors.launcher.game.domain.GameDomainModule
import app.vercors.launcher.game.presentation.GamePresentationModule
import org.koin.core.annotation.Module

@Module(includes = [GameDataModule::class, GameDomainModule::class, GamePresentationModule::class])
class GameModule