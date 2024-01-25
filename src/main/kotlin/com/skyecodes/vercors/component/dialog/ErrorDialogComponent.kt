package com.skyecodes.vercors.component.dialog

import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext

interface ErrorDialogComponent {
    val title: String
    val message: List<String>
    val onClose: () -> Unit
}

class DefaultErrorDialogComponent(
    componentContext: AppComponentContext,
    override val title: String,
    override val message: List<String>,
    override val onClose: () -> Unit,
) : AbstractComponent(componentContext), ErrorDialogComponent