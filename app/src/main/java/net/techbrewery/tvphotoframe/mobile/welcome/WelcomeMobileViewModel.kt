package net.techbrewery.tvphotoframe.mobile.welcome

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.core.BaseViewModel
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import timber.log.Timber

class WelcomeMobileViewModel(private val photosRepository: PhotosRepository) : BaseViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    fun syncPhotos() {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler { error ->
            //TODO
            Timber.e(error)
        }) {
            val photosOfFamily = withContext(Dispatchers.IO) { photosRepository.getLatestPhotos() }

            val user = firebaseAuth.currentUser!!
            photosRepository.insertPhotos(user, photosOfFamily)
            DevDebugLog.log("Insert completed")
//            photosOfFamily.mediaItems.forEach { mediaItem ->
//                DevDebugLog.log("Photo found: $mediaItem")
//            }
        }
    }
}