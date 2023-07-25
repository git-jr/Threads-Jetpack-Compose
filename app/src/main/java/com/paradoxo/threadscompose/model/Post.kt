package com.paradoxo.threadscompose.model

data class Post(
    val id: String = "",
    val userAccount: UserAccount = UserAccount(),
    val description: String = "",
    val date: Long = 0L,
    val medias: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val comments: List<Post> = emptyList()
)

