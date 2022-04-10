package net.techbrewery.tvphotoframe.mobile.welcome

import android.accounts.AccountManager
import net.techbrewery.tvphotoframe.core.BaseViewModel

class WelcomeMobileViewModel(private val accountManager: AccountManager) : BaseViewModel() {

//    fun onSignInClicked() {
//        val accounts = accountManager.accounts
//        DevDebugLog.log("Accounts retrieved: ${accounts.size}")
//        accounts.forEach { account ->
//            DevDebugLog.log("Account retrieved: $account")
//        }
//    }

//    fun getAccessToken(accountName: String, accountType: String) {
//        val account = Account(accountName, accountType)
////        accountManager.getAuthToken(
////            account,                     // Account retrieved using getAccountsByType()
////            "Manage your tasks",            // Auth scope
////            null,                        // Authenticator-specific options
////            true,                           // Your activity
////            object :
////                AccountManagerCallback<Bundle>,              // Callback called when a token is successfully acquired
////                Handler()              // Callback called if an error occurs
////        )
//        accountManager.getAuthToken(
//            account,                     // Account retrieved using getAccountsByType()
//            "https://www.googleapis.com/auth/photoslibrary.readonly",            // Auth scope
//            null,                        // Authenticator-specific options
//            this,                           // Your activity
//            OnTokenAcquired(),              // Callback called when a token is successfully acquired
//            Handler(Looper.getMainLooper())              // Callback called if an error occurs
//        )
//    }
}