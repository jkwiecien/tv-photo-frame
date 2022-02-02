package net.techbrewery.tvphotoframe.core.logs

import timber.log.Timber

class DebugLogsTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "TVPF_LOG"
    }
}