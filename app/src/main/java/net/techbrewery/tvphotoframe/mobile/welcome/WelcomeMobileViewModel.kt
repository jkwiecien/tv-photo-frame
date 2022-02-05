package net.techbrewery.tvphotoframe.mobile.welcome

import android.accounts.AccountManager
import net.techbrewery.tvphotoframe.core.BaseViewModel
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog

class WelcomeMobileViewModel(private val accountManager: AccountManager) : BaseViewModel() {

    fun onSignInClicked() {
        

        val accounts = accountManager.accounts
        DevDebugLog.log("Accounts retrieved: ${accounts.size}")
        accounts.forEach { account ->
            DevDebugLog.log("Account retrieved: $account")
        }
//        accountManager.getAuthToken(
//            myAccount_,                     // Account retrieved using getAccountsByType()
//            "Manage your tasks",            // Auth scope
//            options,                        // Authenticator-specific options
//            this,                           // Your activity
//            OnTokenAcquired(),              // Callback called when a token is successfully acquired
//            Handler(OnError())              // Callback called if an error occurs
//        )
    }
}