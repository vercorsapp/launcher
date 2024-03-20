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

import app.vercors.account.LoginUseCase
import app.vercors.account.LoginUseCaseImpl
import app.vercors.di.DIBuilder
import app.vercors.di.inject
import app.vercors.di.single
import app.vercors.dialog.DialogManager
import app.vercors.dialog.DialogManagerImpl
import app.vercors.home.LoadHomeSectionUseCase
import app.vercors.home.LoadHomeSectionUseCaseImpl
import app.vercors.instance.LaunchInstanceUseCase
import app.vercors.instance.LaunchInstanceUseCaseImpl
import app.vercors.instance.LoadInstancesUseCase
import app.vercors.instance.LoadInstancesUseCaseImpl
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
        single<LoginUseCase> { LoginUseCaseImpl(inject(), inject(), properties.getProperty("microsoftClientId")) }
        single<LoadHomeSectionUseCase> { LoadHomeSectionUseCaseImpl(inject(), inject(), inject()) }
        single<LaunchInstanceUseCase> {
            LaunchInstanceUseCaseImpl(
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
        single<LoadInstancesUseCase> { LoadInstancesUseCaseImpl(inject(), inject(), inject()) }
    }
}