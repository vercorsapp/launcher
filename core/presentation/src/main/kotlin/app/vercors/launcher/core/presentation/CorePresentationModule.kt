package app.vercors.launcher.core.presentation

import app.vercors.launcher.core.generated.resources.Res
import app.vercors.launcher.core.presentation.navigation.AppDestination
import app.vercors.launcher.core.presentation.navigation.DefaultNavigator
import app.vercors.launcher.core.presentation.navigation.Navigator
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class CorePresentationModule

@Single
fun provideNavigator(): Navigator = DefaultNavigator(AppDestination.Home)

val CoreString = Res.string
val CoreDrawable = Res.drawable