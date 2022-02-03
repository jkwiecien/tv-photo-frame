package net.techbrewery.tvphotoframe.core.extensions

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}