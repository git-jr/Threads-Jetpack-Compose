package com.paradoxo.threadscompose.model

data class UserAccount(
    val id: Long = 0,
    val name: String = "",
    val userName: String = "",
    val bio: String = "",
    val link: String = "",
    val imageProfileUrl: String = "",
    val posts:List<Long> = emptyList(),
    val follows:List<Long> = emptyList(),
    val followers:List<Long> = emptyList()
)

