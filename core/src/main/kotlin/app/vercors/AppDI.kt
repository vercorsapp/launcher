package app.vercors

import app.vercors.account.*
import app.vercors.account.auth.AuthenticationService
import app.vercors.account.auth.AuthenticationServiceImpl
import app.vercors.common.AppComponentContext
import app.vercors.common.AppComponentContextImpl
import app.vercors.configuration.*
import app.vercors.di.DI
import app.vercors.di.factory
import app.vercors.di.inject
import app.vercors.di.single
import app.vercors.dialog.DialogComponent
import app.vercors.dialog.DialogComponentImpl
import app.vercors.dialog.DialogService
import app.vercors.dialog.DialogServiceImpl
import app.vercors.dialog.error.ErrorDialogComponent
import app.vercors.dialog.error.ErrorDialogComponentImpl
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogComponentImpl
import app.vercors.dialog.login.LoginDialogComponent
import app.vercors.dialog.login.LoginDialogComponentImpl
import app.vercors.home.HomeComponent
import app.vercors.home.HomeComponentImpl
import app.vercors.home.HomeService
import app.vercors.home.HomeServiceImpl
import app.vercors.instance.*
import app.vercors.instance.details.InstanceDetailsComponent
import app.vercors.instance.details.InstanceDetailsComponentImpl
import app.vercors.instance.launch.LauncherService
import app.vercors.instance.launch.LauncherServiceImpl
import app.vercors.instance.mojang.MojangService
import app.vercors.instance.mojang.MojangServiceImpl
import app.vercors.menu.MenuComponent
import app.vercors.menu.MenuComponentImpl
import app.vercors.navigation.NavigationComponent
import app.vercors.navigation.NavigationComponentImpl
import app.vercors.navigation.NavigationService
import app.vercors.navigation.NavigationServiceImpl
import app.vercors.project.ProjectDetailsComponent
import app.vercors.project.ProjectDetailsComponentImpl
import app.vercors.project.SearchComponent
import app.vercors.project.SearchComponentImpl
import app.vercors.project.curseforge.CurseforgeService
import app.vercors.project.curseforge.CurseforgeServiceImpl
import app.vercors.project.modrinth.ModrinthService
import app.vercors.project.modrinth.ModrinthServiceImpl
import app.vercors.root.RootComponent
import app.vercors.root.RootComponentImpl
import app.vercors.root.error.ErrorComponent
import app.vercors.root.error.ErrorComponentImpl
import app.vercors.root.main.MainComponent
import app.vercors.root.main.MainComponentImpl
import app.vercors.root.setup.SetupComponent
import app.vercors.root.setup.SetupComponentImpl
import app.vercors.system.storage.StorageService
import app.vercors.system.storage.StorageServiceImpl
import app.vercors.system.theme.SystemThemeService
import app.vercors.system.theme.SystemThemeServiceImpl
import app.vercors.toolbar.ToolbarComponent
import app.vercors.toolbar.ToolbarComponentImpl
import ca.gosyer.appdirs.AppDirs
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import java.util.*

fun appDI(properties: Properties, coroutineScope: CoroutineScope) = DI(coroutineScope) {
    single<CoroutineScope> { coroutineScope }
    single<Json> { AppJson }
    single<HttpClient> { appHttpClient(inject()) }
    single<AppDirs> { AppDirs(APP_NAME) }
    single<StorageService> { StorageServiceImpl(inject(), inject()) }
    single<SystemThemeService> { SystemThemeServiceImpl(inject()) }
    single<ConfigurationService> { ConfigurationServiceImpl(inject(), inject()) }
    single<ConfigurationRepository> { ConfigurationRepositoryImpl(inject(), inject(), inject()) }
    single<InstanceService> { InstanceServiceImpl(inject(), inject(), inject()) }
    single<InstanceRepository> { InstanceRepositoryImpl(inject(), inject()) }
    single<LauncherService> { LauncherServiceImpl(inject(), inject(), inject(), inject(), inject(), inject()) }
    single<AccountService> { AccountServiceImpl(inject(), inject()) }
    single<AccountRepository> { AccountRepositoryImpl(inject(), inject()) }
    single<AuthenticationService> { AuthenticationServiceImpl(inject(), properties.getProperty("microsoftClientId")) }
    single<NavigationService> { NavigationServiceImpl(inject(), inject()) }
    single<DialogService> { DialogServiceImpl() }
    single<HomeService> { HomeServiceImpl(inject(), inject(), inject()) }
    single<MojangService> { MojangServiceImpl(inject(), inject()) }
    single<ModrinthService> { ModrinthServiceImpl(inject(), properties.getProperty("modrinthApiKey")) }
    single<CurseforgeService> { CurseforgeServiceImpl(inject(), properties.getProperty("curseforgeApiKey")) }
    factory<AppComponentContext> { AppComponentContextImpl(param(), param()) }
    factory<RootComponent> { RootComponentImpl(param(), param()) }
    factory<ErrorComponent> { ErrorComponentImpl(param(), param(), param()) }
    factory<SetupComponent> { SetupComponentImpl(param(), param(), param()) }
    factory<MainComponent> { MainComponentImpl(param(), param()) }
    factory<MenuComponent> { MenuComponentImpl(param(), param()) }
    factory<ToolbarComponent> { ToolbarComponentImpl(param(), param()) }
    factory<NavigationComponent> { NavigationComponentImpl(param()) }
    factory<DialogComponent> { DialogComponentImpl(param()) }
    factory<CreateInstanceDialogComponent> { CreateInstanceDialogComponentImpl(param(), param()) }
    factory<LoginDialogComponent> { LoginDialogComponentImpl(param(), param()) }
    factory<ErrorDialogComponent> { ErrorDialogComponentImpl(param()) }
    factory<AccountsComponent> { AccountsComponentImpl(param()) }
    factory<HomeComponent> { HomeComponentImpl(param()) }
    factory<InstanceListComponent> { InstanceListComponentImpl(param()) }
    factory<InstanceDetailsComponent> { InstanceDetailsComponentImpl(param()) }
    factory<SearchComponent> { SearchComponentImpl(param()) }
    factory<ProjectDetailsComponent> { ProjectDetailsComponentImpl(param()) }
    factory<ConfigurationComponent> { ConfigurationComponentImpl(param()) }
}