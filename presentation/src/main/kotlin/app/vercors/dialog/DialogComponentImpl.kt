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

package app.vercors.dialog

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
import app.vercors.dialog.instance.create.CreateInstanceDialogComponent
import app.vercors.dialog.instance.kill.KillInstanceDialogComponent
import app.vercors.dialog.login.LoginDialogComponent
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.StateFlow

internal class DialogComponentImpl(
    componentContext: AppComponentContext,
    private val dialogManager: DialogManager = componentContext.inject()
) : AbstractAppComponent(componentContext, KotlinLogging.logger {}), DialogComponent {
    private val navigation = SlotNavigation<DialogConfig>()
    private val _childState: Value<ChildSlot<DialogConfig, DialogChildComponent>> = childSlot(
        source = navigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    )
    override val state: StateFlow<DialogState> = _childState.map { DialogState(it.child?.instance) }.toStateFlow()

    init {
        dialogManager.dialogState.collectInLifecycle { config ->
            config?.let { navigation.activate(it) } ?: navigation.dismiss()
        }
    }

    private fun createChild(config: DialogConfig, componentContext: AppComponentContext): DialogChildComponent =
        when (config) {
            DialogConfig.CreateInstance -> inject<CreateInstanceDialogComponent>(
                componentContext,
                dialogManager::closeDialog
            )

            is DialogConfig.Login -> inject<LoginDialogComponent>(componentContext, {
                dialogManager.closeDialog()
                config.onAuthenticationFinished?.invoke()
            })

            is DialogConfig.KillInstance -> inject<KillInstanceDialogComponent>(
                componentContext,
                config.onKill,
                dialogManager::closeDialog
            )

            DialogConfig.Error.Launch -> inject<LaunchErrorDialogComponent>(
                componentContext,
                dialogManager::closeDialog
            )

            is DialogConfig.Error.JavaVersion -> inject<JavaVersionErrorDialogComponent>(
                componentContext,
                dialogManager::closeDialog,
                config.instanceId,
                config.javaVersion
            )
        }
}