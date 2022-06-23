package net.techbrewery.tvphotoframe.tv.welcome

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.core.BaseViewModel
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.network.OAuth2APi


class WelcomeViewModel(private val authApi: OAuth2APi) : BaseViewModel() {

    fun onAuthorizeClicked() {
        viewModelScope.launch(Dispatchers.Main) {
            val deviceCodeResponse = withContext(Dispatchers.IO) { authApi.getDeviceCode() }
            DevDebugLog.log("Device code received: $deviceCodeResponse")
        }
    }
}