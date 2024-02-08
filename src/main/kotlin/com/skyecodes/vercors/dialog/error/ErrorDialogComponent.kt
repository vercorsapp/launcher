package com.skyecodes.vercors.dialog.error

import com.skyecodes.vercors.root.AbstractComponent
import com.skyecodes.vercors.root.AppComponentContext

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