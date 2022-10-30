package net.techbrewery.tvphotoframe.mobile.photos

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.database.getLongOrNull
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.core.BaseViewModel
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog

class GallerySyncViewModel(private val contentResolver: ContentResolver) : BaseViewModel() {

    var photosUris by mutableStateOf<List<Uri>>(emptyList())
        private set

    fun loadGallery() {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler { error ->
            //TODO
        }) {
            photosUris = withContext(Dispatchers.Default) { fetchPhotosFromStorage() }
        }
        DevDebugLog.log("fetchPhotos()")
    }

    private suspend fun fetchPhotosFromStorage(): List<Uri> {
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE,
        )

        val selection =
            "${MediaStore.Images.Media.DATE_TAKEN} != ? or ${MediaStore.Images.Media.DATE_ADDED} != ?"
        val selectionArgs = arrayOf("NULL", "NULL")


        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} ASC"
        val uris = mutableListOf<Uri>()

        contentResolver.query(
            collection,
            projection,
            selection, // Which rows to return (all rows)
            selectionArgs, // Selection arguments (none)
            sortOrder
        )!!.use { cursor ->
            DevDebugLog.log("Cursor ready: ${cursor.count}")
            while (cursor.moveToNext()) {
                // Use an ID column from the projection to get
                // a URI representing the media item itself.
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                val dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)

                val id: Long = cursor.getLong(idColumn)
                val dateTaken: Long? = cursor.getLongOrNull(dateTakenColumn)
                val dateAdded: Long = cursor.getLong(dateAddedColumn)
                val size: Int = cursor.getInt(sizeColumn)

                val date: Long = dateTaken ?: dateAdded
                val uniqueId: String = "${date}-${size}"

                val uri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                uris.add(uri)
                DevDebugLog.log("Fetched photo: $uri")
            }
        }
        return uris
    }

}