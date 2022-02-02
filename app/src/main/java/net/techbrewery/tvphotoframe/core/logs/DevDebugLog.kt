package net.techbrewery.tvphotoframe.core.logs

import net.techbrewery.tvphotoframe.BuildConfig
import timber.log.Timber

object DevDebugLog {
    fun log(message: String) {
        if (BuildConfig.DEBUG) Timber.v("TVPF dev-debug| $message")
    }
}