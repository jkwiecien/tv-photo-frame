package net.techbrewery.tvphotoframe.core.extensions

import android.os.Bundle
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog

fun Bundle.logAllExtras() {
    for (key in keySet()) {
        DevDebugLog.log(key + " : " + if (get(key) != null) get(key) else "NULL")
    }
}