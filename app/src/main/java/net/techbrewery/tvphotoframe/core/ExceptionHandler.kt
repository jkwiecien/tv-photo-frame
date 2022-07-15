package net.techbrewery.tvphotoframe.core

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@Suppress("FunctionName")
inline fun ExceptionHandler(
    viewModelScope: CoroutineScope,
    crossinline handler: (CoroutineContext, Throwable) -> Unit
): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) {
            viewModelScope.launch(Dispatchers.Main) {
                handler.invoke(context, exception)
            }
        }
    }