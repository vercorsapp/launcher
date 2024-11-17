package app.vercors.launcher.account.presentation

import app.vercors.launcher.account.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class AccountPresentationModule

val AccountString = Res.string
val AccountDrawable = Res.drawable