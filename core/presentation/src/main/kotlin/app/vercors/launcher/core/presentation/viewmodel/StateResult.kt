package app.vercors.launcher.core.presentation.viewmodel

sealed interface StateResult<State, Effect> {
    val effects: List<Effect>

    data class Unchanged<S, E>(override val effects: List<E> = emptyList()) : StateResult<S, E> {
        constructor(vararg effects: E) : this(listOf(*effects))
    }

    data class Changed<S, E>(val newState: S, override val effects: List<E> = emptyList()) : StateResult<S, E> {
        constructor(newState: S, vararg effects: E) : this(newState, listOf(*effects))
    }
}