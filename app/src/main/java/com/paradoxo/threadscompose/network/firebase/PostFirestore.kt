package com.paradoxo.threadscompose.network.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.paradoxo.threadscompose.model.Post

class PostFirestore {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val dbPosts = firebaseFirestore.collection("posts")

    fun getAllPosts(
        onSuccess: (List<Post>) -> Unit = {},
        onError: () -> Unit = {},
    ) {
        dbPosts.get()
            .addOnSuccessListener {
                val posts = it.toObjects(Post::class.java)
                onSuccess(posts)
                Log.i("getAllPosts", "Posts obtidos com sucesso")
            }
            .addOnFailureListener {
                onError()
                Log.i("getAllPosts", "Erro ao obter posts ${it.message}")
            }
    }


    fun insertPost(
        post: Post,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        dbPosts.document()
            .set(post)
            .addOnSuccessListener {
                onSuccess()
                Log.i("insertPost", "Post inserido com sucesso")
            }
            .addOnFailureListener {
                onError()
                Log.i("insertPost", "Erro ao inserir post ${it.message}")
            }
    }
}