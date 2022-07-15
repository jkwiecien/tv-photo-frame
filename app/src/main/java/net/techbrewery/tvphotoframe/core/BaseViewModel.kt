package net.techbrewery.tvphotoframe.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    private val eventsMutableFlow: MutableStateFlow<StateFlowEvent<*>> =
        MutableStateFlow(StateFlowEvent.empty())
    val eventsFlow: StateFlow<StateFlowEvent<*>> = eventsMutableFlow

    internal fun sendEvent(payload: Any) {
        eventsMutableFlow.value = StateFlowEvent(payload)
    }

    internal fun exceptionHandler(onError: (Throwable) -> Unit): CoroutineExceptionHandler =
        ExceptionHandler(viewModelScope) { _, error ->
            Timber.e(error)
            onError(error)
        }
}