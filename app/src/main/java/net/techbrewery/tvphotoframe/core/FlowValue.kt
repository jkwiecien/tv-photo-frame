package net.techbrewery.tvphotoframe.core

sealed class FlowValue<T> {
    class Value<T>(val value: T) : FlowValue<T>()
    class Empty<T> : FlowValue<T>()
}
