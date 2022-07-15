package net.techbrewery.tvphotoframe.mobile.welcome

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.techbrewery.tvphotoframe.core.firestore.Photo
import net.techbrewery.tvphotoframe.core.koin.PhotosApiProvider
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.network.PhotosApi
import net.techbrewery.tvphotoframe.network.requests.PhotosSearchRequestBody
import net.techbrewery.tvphotoframe.network.responses.MediaItemApiModel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PhotosRepository {
    private val api get() = PhotosApiProvider.photosApi

    suspend fun getLatestPhotos(): List<MediaItemApiModel> {
//        return api.getPhotosInAlbum(
//            albumId = PhotosApi.TEMP_FAMILY_AND_FRIENDS_HARDCODED_ID,
//            pageSize = 5
//        ).mediaItems

        return api
            .getPhotosInAlbum(
                PhotosSearchRequestBody(
                    albumId = PhotosApi.TEMP_FAMILY_AND_FRIENDS_HARDCODED_ID,
                    pageSize = 5
                )
            )
            .mediaItems
            .filter { it.mediaMetadata.photo != null }
    }

    suspend fun insertPhotos(user: FirebaseUser, mediaItems: List<MediaItemApiModel>): List<Photo> {
        return suspendCoroutine { continuation ->


            val db = Firebase.firestore
            val batch = db.batch()

            val photosCollectionRef = db
                .collection("users")
                .document(user.uid)
                .collection("photos")

            val photos = mediaItems
                .map {
                    DevDebugLog.log("Inserting: $it")
                    Photo.with(it)
                }

            photos.forEach { photo ->
                val documentRef = photosCollectionRef.document(photo.id)
                batch.set(documentRef, photo)
            }

            batch.commit()
                .addOnSuccessListener { continuation.resume(photos) }
                .addOnFailureListener { error -> continuation.resumeWithException(error) }

//            Firebase.firestore
//                .collection("users")
//                .document(user.uid)
//                .collection("photos")
//                .set(photos)
//                .addOnSuccessListener { continuation.resume(photos) }
//                .addOnFailureListener { error -> continuation.resumeWithException(error) }
        }
    }
}