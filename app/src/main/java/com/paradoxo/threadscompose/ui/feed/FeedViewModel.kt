package com.paradoxo.threadscompose.ui.feed

import androidx.lifecycle.ViewModel
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
    }
}
