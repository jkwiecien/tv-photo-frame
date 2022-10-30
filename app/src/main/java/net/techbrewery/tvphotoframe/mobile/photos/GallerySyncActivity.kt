package net.techbrewery.tvphotoframe.mobile.photos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Size
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.ui.components.MobileViewRoot
import net.techbrewery.tvphotoframe.core.ui.theme.SpacingMobile
import org.koin.androidx.viewmodel.ext.android.viewModel

class GallerySyncActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, GallerySyncActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private val viewModel by viewModel<GallerySyncViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GallerySyncView(
                photos = viewModel.photosUris
            )
        }
        checkRequiredRuntimePermissions()
    }

    private fun checkRequiredRuntimePermissions() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.loadGallery()
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    //TODO
                }
            }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.loadGallery()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}

@Composable
private fun GallerySyncView(photos: List<Uri>) {
    MobileViewRoot {
        LazyColumn(Modifier.fillMaxSize()) {
            items(photos) { photoUri ->
                Image(
                    bitmap = LocalContext.current.contentResolver.loadThumbnail(
                        photoUri,
                        Size(100, 100),
                        null
                    )
                        .asImageBitmap(),
                    contentDescription = "Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(SpacingMobile.Large),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}