package com.paradoxo.threadscompose.ui.feed

import com.paradoxo.threadscompose.model.Post

data class FeedScreenState(
    var posts: List<Post> = emptyList(),
)
