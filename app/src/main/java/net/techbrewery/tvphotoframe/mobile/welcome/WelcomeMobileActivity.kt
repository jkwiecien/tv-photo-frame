package net.techbrewery.tvphotoframe.mobile.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.network.OAuth2APi.Companion.AUTH_URL
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeMobileActivity : BaseActivity() {


    private val viewModel by viewModel<WelcomeMobileViewModel>()

//    private val startSignIn =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val gsa = GoogleSignIn
//                    .getSignedInAccountFromIntent(result.data)
//                    .getResult(ApiException::class.java)
//                onLoggedIn(gsa)
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAuth()

//        val gso = GoogleSignIn.getLastSignedInAccount(this)
//        if (gso != null) startAuth()
//
//        setContent {
//            AppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Column {
//                        if (gso == null) {
//                            GoogleSignInButton(
//                                onSignInClicked = { startAuth() }
//                            )
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun startAuth() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL.toString()))
        startActivity(browserIntent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == RESULT_OK && requestCode == RequestCodes.GOOGLE_PHOTOS_AUTH && data != null) {
//            DevDebugLog.log("Authorized Google Photos")
//            val gsa =
//                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
//            DevDebugLog.log("ID token: ${gsa.idToken}")
////            DevDebugLog.log("Data in result: ${data?.extras?.logAllExtras()}")
//        }
//    }

}