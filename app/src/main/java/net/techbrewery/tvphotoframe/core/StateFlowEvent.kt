package net.techbrewery.tvphotoframe.core


open class StateFlowEvent<out T>(private val content: T) {

    companion object {
        fun empty(): StateFlowEvent<String> = StateFlowEvent("TVPF-empty-state-flow-event")
    }

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun setHandled() {
        hasBeenHandled = true
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}