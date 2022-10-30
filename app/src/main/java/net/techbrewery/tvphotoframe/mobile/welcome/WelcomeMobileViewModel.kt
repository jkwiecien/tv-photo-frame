package net.techbrewery.tvphotoframe.mobile.welcome

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.techbrewery.tvphotoframe.core.BaseViewModel

class WelcomeMobileViewModel(private val photosRepository: PhotosRepository) : BaseViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    fun syncPhotos() {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler { error ->
            //TODO
        }) {
            
        }
    }
}