package com.paradoxo.threadscompose.network.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.paradoxo.threadscompose.model.Comment
import com.paradoxo.threadscompose.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.UUID

class PostFirestore {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val dbPosts = firebaseFirestore.collection("posts")

    private val mediaFirebaseStorage = MediaFirebaseStorage()

    fun getAllPosts(
        onSuccess: (List<Post>) -> Unit = {},
        onError: () -> Unit = {},
    ) {
        dbPosts
            .whereEqualTo("mainPost", true)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val posts = it.toObjects(Post::class.java)
                onSuccess(posts)
                Log.i("getAllPosts", "Posts obtidos com sucesso ${posts.size}")
            }
            .addOnFailureListener {
                onError()
                Log.i("getAllPosts", "Erro ao obter posts ${it.message}")
            }
    }

    fun savePost(
        posts: List<Post>,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        if (posts.size > 1) {
            savePostThread(posts, onSuccess, onError)
        } else {

            CoroutineScope(IO).launch {
                val post = posts.first().copy(
                    id = UUID.randomUUID().toString(),
                    mainPost = true,
                    medias = mediaFirebaseStorage.uploadMedia(posts.first().medias)
                )
                dbPosts.document()
                    .set(post)
                    .addOnSuccessListener {
                        onSuccess()
                        Log.i("savePost", "Post salvo com sucesso")
                    }
                    .addOnFailureListener {
                        onError()
                        Log.i("savePost", "Erro ao salvar post ${it.message}")
                    }
            }
        }
    }

    private fun savePostThread(
        posts: List<Post>,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val batch = firebaseFirestore.batch()

        val postsInThreadFormat: List<Post> = generatePostsInThreadFormat(posts)

        CoroutineScope(IO).launch {
            try {
                postsInThreadFormat.forEach { post ->
                    val urls = mediaFirebaseStorage.uploadMedia(post.medias)
                    val newPost = post.copy(medias = urls)
                    val documentReference = dbPosts.document()
                    batch.set(documentReference, newPost)
                }

                batch.commit()
                    .addOnSuccessListener {
                        onSuccess()
                        Log.i("savePostThread", "Thread salvo com sucesso")
                    }
                    .addOnFailureListener {
                        onError()
                        Log.i("savePostThread", "Erro ao salvar Thread ${it.message}")
                    }
            } catch (e: Exception) {
                Log.e("savePostThread", "Erro ao salvar Thread ${e.message}")
            }
        }
    }

    private fun generatePostsInThreadFormat(posts: List<Post>): List<Post> {
        val postsInThreadFormat = mutableListOf<Post>()
        val randomIdList: List<String> = mutableListOf<String>().apply {
            repeat(posts.size) {
                add(UUID.randomUUID().toString())
            }
        }

        posts.forEachIndexed { index, post ->

            val commentList = mutableListOf<Comment>()
            randomIdList.subList(index.plus(1), posts.size).forEach { id ->
                commentList.add(
                    Comment(
                        id = id,
                        profilePicAuthor = post.userAccount.imageProfileUrl,
                    )
                )
            }

            postsInThreadFormat.add(
                post.copy(
                    id = randomIdList[index],
                    mainPost = index == 0,
                    comments = commentList,
                    likes = emptyList()
                )
            )
        }

        return postsInThreadFormat
    }
}