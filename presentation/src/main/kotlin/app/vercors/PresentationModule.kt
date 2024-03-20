/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors

import app.vercors.account.AccountListComponent
import app.vercors.account.AccountListComponentImpl
import app.vercors.common.AppComponentContext
import app.vercors.common.AppComponentContextImpl
import app.vercors.configuration.ConfigurationComponent
import app.vercors.configuration.ConfigurationComponentImpl
import app.vercors.di.DIBuilder
import app.vercors.di.factory
import app.vercors.dialog.DialogComponent
import app.vercors.dialog.DialogComponentImpl
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponentImpl
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogComponentImpl
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogComponentImpl
import app.vercors.dialog.login.LoginDialogComponent
import app.vercors.dialog.login.LoginDialogComponentImpl
import app.vercors.home.HomeComponent
import app.vercors.home.HomeComponentImpl
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.InstanceListComponentImpl
import app.vercors.instance.details.InstanceDetailsComponent
import app.vercors.instance.details.InstanceDetailsComponentImpl
import app.vercors.menu.MenuComponent
import app.vercors.menu.MenuComponentImpl
import app.vercors.navigation.NavigationComponent
import app.vercors.navigation.NavigationComponentImpl
import app.vercors.notification.NotificationListComponent
import app.vercors.notification.NotificationListComponentImpl
import app.vercors.project.ProjectDetailsComponent
import app.vercors.project.ProjectDetailsComponentImpl
import app.vercors.project.SearchComponent
import app.vercors.project.SearchComponentImpl
import app.vercors.root.RootComponent
import app.vercors.root.RootComponentImpl
import app.vercors.root.error.ErrorComponent
import app.vercors.root.error.ErrorComponentImpl
import app.vercors.root.main.MainComponent
import app.vercors.root.main.MainComponentImpl
import app.vercors.root.setup.SetupComponent
import app.vercors.root.setup.SetupComponentImpl
import app.vercors.toolbar.ToolbarComponent
import app.vercors.toolbar.ToolbarComponentImpl
import java.util.*

fun DIBuilder.PresentationModule(properties: Properties) {
    DomainModule(properties)
    module {
        factory<AppComponentContext> { AppComponentContextImpl(param(), param()) }
        factory<RootComponent> { RootComponentImpl(param(), param()) }
        factory<ErrorComponent> { ErrorComponentImpl(param(), param(), param()) }
        factory<SetupComponent> { SetupComponentImpl(param(), param()) }
        factory<MainComponent> { MainComponentImpl(param(), param()) }
        factory<MenuComponent> { MenuComponentImpl(param(), param()) }
        factory<ToolbarComponent> { ToolbarComponentImpl(param(), param(), param()) }
        factory<NavigationComponent> { NavigationComponentImpl(param()) }
        factory<DialogComponent> { DialogComponentImpl(param()) }
        factory<CreateInstanceDialogComponent> { CreateInstanceDialogComponentImpl(param(), param()) }
        factory<LoginDialogComponent> { LoginDialogComponentImpl(param(), param()) }
        factory<LaunchErrorDialogComponent> { LaunchErrorDialogComponentImpl(param(), param()) }
        factory<JavaVersionErrorDialogComponent> {
            JavaVersionErrorDialogComponentImpl(
                param(),
                param(),
                param(),
                param()
            )
        }
        factory<AccountListComponent> { AccountListComponentImpl(param()) }
        factory<NotificationListComponent> { NotificationListComponentImpl(param()) }
        factory<HomeComponent> { HomeComponentImpl(param()) }
        factory<InstanceListComponent> { InstanceListComponentImpl(param()) }
        factory<InstanceDetailsComponent> { InstanceDetailsComponentImpl(param()) }
        factory<SearchComponent> { SearchComponentImpl(param()) }
        factory<ProjectDetailsComponent> { ProjectDetailsComponentImpl(param()) }
        factory<ConfigurationComponent> { ConfigurationComponentImpl(param()) }
    }
}