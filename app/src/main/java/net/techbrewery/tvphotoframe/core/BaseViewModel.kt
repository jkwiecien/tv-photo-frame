package net.techbrewery.tvphotoframe.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    private val eventsMutableFlow: MutableStateFlow<StateFlowEvent<*>> =
        MutableStateFlow(StateFlowEvent.empty())
    val eventsFlow: StateFlow<StateFlowEvent<*>> = eventsMutableFlow

    internal fun sendEvent(payload: Any) {
        eventsMutableFlow.value = StateFlowEvent(payload)
    }
}