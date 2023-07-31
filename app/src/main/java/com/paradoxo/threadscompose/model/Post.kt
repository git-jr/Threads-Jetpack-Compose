package com.paradoxo.threadscompose.model

data class Post(
    val id: String = "",
    val mainPost: Boolean = false,
    val userAccount: UserAccount = UserAccount(),
    val description: String = "",
    val date: Long = 0L,
    val medias: List<String> = emptyList(),
    val likes: List<Like> = emptyList(),
    val comments: List<Comment> = emptyList()
)

data class Like(
    val id: String = "",
    val profilePicAuthor: String = "",
)

data class Comment(
    val id: String = "",
    val profilePicAuthor: String = "",
)
