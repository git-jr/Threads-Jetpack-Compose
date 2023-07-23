package com.paradoxo.threadscompose.model

data class Post(
    val id: Int = 0,
    val userAccount: UserAccount = UserAccount(),
    val description: String = "",
    val date: Long = 0L,
    val medias: List<String> = emptyList(),
    val likes: List<Long> = emptyList(),
    val comments: List<Post> = emptyList()
)

