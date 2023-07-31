package com.paradoxo.threadscompose.ui.feed

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.model.Like
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.network.firebase.PostFirestore
import com.paradoxo.threadscompose.sampleData.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedScreenState())
    val uiState: StateFlow<FeedScreenState> = _uiState.asStateFlow()

    init {
        val postFirestore = PostFirestore()
        postFirestore.getAllPosts(
            onSuccess = { posts ->
                _uiState.value = _uiState.value.copy(posts = posts)
            },
            onError = {
                _uiState.value = _uiState.value.copy(posts = SampleData().posts)
            }
        )

        _uiState.value = _uiState.value.copy(
            currentUserProfile = UserAccount(
                id = Firebase.auth.currentUser?.uid ?: "",
                userName = Firebase.auth.currentUser?.displayName ?: "",
                imageProfileUrl = Firebase.auth.currentUser?.photoUrl.toString()
            )
        )
    }

    fun likePost(it: Post) {

        if (it.likes.any { like -> like.id == _uiState.value.currentUserProfile.id }) {
            _uiState.value = _uiState.value.copy(posts = _uiState.value.posts.map { post ->
                if (post.id == it.id) {
                    post.copy(
                        likes = post.likes.toMutableList().apply {
                            removeIf { like -> like.id == _uiState.value.currentUserProfile.id }
                        }
                    )
                } else {
                    post
                }
            })
            return
        }

        _uiState.value = _uiState.value.copy(posts = _uiState.value.posts.map { post ->
            if (post.id == it.id) {
                post.copy(
                    likes = post.likes.toMutableList().apply {
                        add(
                            Like(
                                id = _uiState.value.currentUserProfile.id,
                                profilePicAuthor = _uiState.value.currentUserProfile.imageProfileUrl
                            )
                        )
                    }
                )
            } else {
                post
            }
        })
    }
}
