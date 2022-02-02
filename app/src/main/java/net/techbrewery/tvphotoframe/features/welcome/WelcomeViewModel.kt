package net.techbrewery.tvphotoframe.features.welcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.techbrewery.tvphotoframe.core.BaseViewModel

class WelcomeViewModel : BaseViewModel() {
    var emailState by mutableStateOf("")
        private set

    fun setEmail(email: String) {
        if (emailState != email) emailState = email
    }

    var passwordState by mutableStateOf("")
        private set

    fun setPassword(password: String) {
        if (passwordState != password) passwordState = password
    }
}