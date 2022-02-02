package net.techbrewery.tvphotoframe.features.welcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.techbrewery.tvphotoframe.core.BaseViewModel

class WelcomeViewModel : BaseViewModel() {
    var emailState by mutableStateOf("")
        private set

    fun setEmail(email: String) {
        emailState = email
    }
//    val emailFlow: StateFlow<String> = emailMutableFlow

    private val passwordMutableFlow = MutableStateFlow("")
    val passwordFlow: StateFlow<String> = passwordMutableFlow
}