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

import app.vercors.account.auth.AuthenticateUseCase
import app.vercors.account.auth.AuthenticateUseCaseImpl
import app.vercors.account.auth.ValidateTokenUseCase
import app.vercors.account.auth.ValidateTokenUseCaseImpl
import app.vercors.di.DIBuilder
import app.vercors.di.inject
import app.vercors.di.single
import app.vercors.dialog.DialogManager
import app.vercors.dialog.DialogManagerImpl
import app.vercors.home.LoadInstancesHomeSectionUseCase
import app.vercors.home.LoadInstancesHomeSectionUseCaseImpl
import app.vercors.home.LoadProjectsHomeSectionUseCase
import app.vercors.home.LoadProjectsHomeSectionUseCaseImpl
import app.vercors.instance.*
import app.vercors.navigation.NavigationManager
import app.vercors.navigation.NavigationManagerImpl
import app.vercors.notification.NotificationManager
import app.vercors.notification.NotificationManagerImpl
import app.vercors.system.SystemThemeManager
import app.vercors.system.SystemThemeManagerImpl
import java.util.*

fun DIBuilder.DomainModule(properties: Properties) {
    DataModule(properties)
    module {
        single<NotificationManager> { NotificationManagerImpl() }
        single<DialogManager> { DialogManagerImpl() }
        single<NavigationManager> { NavigationManagerImpl(inject(), inject()) }
        single<SystemThemeManager> { SystemThemeManagerImpl(inject()) }
        single<AuthenticateUseCase> { AuthenticateUseCaseImpl(inject()) }
        single<ValidateTokenUseCase> { ValidateTokenUseCaseImpl(inject()) }
        single<LoadProjectsHomeSectionUseCase> { LoadProjectsHomeSectionUseCaseImpl(inject(), inject()) }
        single<LoadInstancesHomeSectionUseCase> { LoadInstancesHomeSectionUseCaseImpl(inject()) }
        single<PrepareInstanceUseCase> {
            PrepareInstanceUseCaseImpl(
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject()
            )
        }
        single<LaunchInstanceUseCase> {
            LaunchInstanceUseCaseImpl(
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
                inject()
            )
        }
        single<LoadInstancesUseCase> { LoadInstancesUseCaseImpl(inject(), inject(), inject()) }
    }
}