package app.vercors.root.error

import app.vercors.root.RootChildComponent

interface ErrorComponent : RootChildComponent {
    val error: Throwable
}