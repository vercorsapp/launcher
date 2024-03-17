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
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.login.LoginDialogComponent
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

class DialogComponentImpl(
    componentContext: AppComponentContext,
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), DialogEventHandler by dialogService, DialogComponent {
    private val navigation = SlotNavigation<DialogConfig>()
    private val _childState: Value<ChildSlot<DialogConfig, DialogChildComponent>> = childSlot(
        source = navigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = childFactory(::createChild)
    )
    override val childState: StateFlow<ChildSlot<*, DialogChildComponent>> = _childState.toStateFlow()

    init {
        dialogService.dialogState.collectInLifecycle { config ->
            config?.let { navigation.activate(it) } ?: navigation.dismiss()
        }
    }

    private fun createChild(config: DialogConfig, componentContext: AppComponentContext): DialogChildComponent =
        when (config) {
            DialogConfig.CreateInstance -> inject<CreateInstanceDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Login -> inject<LoginDialogComponent>(componentContext, ::closeDialog)
            DialogConfig.Error.Launch -> inject<LaunchErrorDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Error.JavaVersion -> inject<JavaVersionErrorDialogComponent>(
                componentContext,
                ::closeDialog,
                config.instance,
                config.javaVersion
            )
        }
}