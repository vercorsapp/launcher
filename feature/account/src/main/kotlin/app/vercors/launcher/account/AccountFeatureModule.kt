package app.vercors.launcher.account

import app.vercors.launcher.account.data.AccountDataModule
import app.vercors.launcher.account.domain.AccountDomainModule
import app.vercors.launcher.account.presentation.AccountPresentationModule
import org.koin.core.annotation.Module

@Module(includes = [AccountDataModule::class, AccountDomainModule::class, AccountPresentationModule::class])
class AccountFeatureModule