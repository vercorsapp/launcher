package app.vercors.launcher.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.domain.DefaultFlowTimeout
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class MviViewModel<State, Event, Effect>(defaultState: State) : ViewModel() {
    protected val logger = KotlinLogging.logger(this::class.qualifiedName!!)

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(defaultState)
    val uiState: StateFlow<State> = _uiState
        .onStart {
            logger.debug { "Initializing data" }
            init()
        }
        .onCompletion {
            logger.debug { "Cleaning up data" }
            cleanup()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(DefaultFlowTimeout), defaultState)

    private val _uiEffects: Channel<Effect> = Channel(capacity = Channel.CONFLATED)
    val uiEffects: Flow<Effect> = _uiEffects.receiveAsFlow()

    fun onEvent(event: Event) {
        logger.debug { "Handling Event: $event" }
        val result = reduce(uiState.value, event)
        logger.debug { "Event Result: $result" }
        if (result is StateResult.Changed) updateState(result.newState)
        result.effects.forEach(::sendEffect)
    }

    protected open fun init() {
        // To override if necessary
    }

    protected open fun cleanup() {
        // To override if necessary
    }

    private fun updateState(state: State) {
        logger.debug { "Updating State: $state" }
        _uiState.tryEmit(state)
    }

    private fun sendEffect(effect: Effect) {
        logger.debug { "Sending Effect: $effect" }
        _uiEffects.trySend(effect)
    }

    /**
     * Take the current state and an intent.
     * Returns the updated state and an optional list of effects.
     */
    protected abstract fun reduce(state: State, event: Event): StateResult<State, Effect>
}

