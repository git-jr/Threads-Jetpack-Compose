package com.paradoxo.threadscompose.network.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class MediaFirebaseStorage {
    private val idCurrentUser = Firebase.auth.currentUser?.uid
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val ref = storageRef.child("users/$idCurrentUser/medias/posts/")

    suspend fun uploadMedia(
        medias: List<String>,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ): List<String> = coroutineScope {
        val dispatcher = Dispatchers.IO

        withContext(dispatcher) {
            val stackTasks = medias.map { imageUri ->
                async {
                    val uuid = UUID.randomUUID().toString()
                    val currentRef = ref.child(uuid)
                    val file = Uri.parse(imageUri)
                    val uploadTask = currentRef.putFile(file)

                    uploadTask.await()
                    val downloadUrl = currentRef.downloadUrl.await().toString()

                    Log.i("imageUpload", "imageUpload link: $downloadUrl")

                    downloadUrl
                }
            }
            try {
                val downloadUrls = stackTasks.awaitAll()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
                downloadUrls
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError()
                }
                Log.i("imageUpload", "imageUpload error: ${e.message}")
                emptyList<String>()
            }
        }
    }
}
