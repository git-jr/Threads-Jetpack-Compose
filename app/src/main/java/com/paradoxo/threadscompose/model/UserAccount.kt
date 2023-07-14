package com.paradoxo.threadscompose.model

data class UserAccount(
    val id: Long = 0,
    val name: String = "",
    val userName: String = "",
    val bio: String = "",
    val imageProfileUrl: Int = 0,
    val posts:List<Long> = emptyList(),
    val follows:List<Long> = emptyList(),
    val followers:List<Long> = emptyList()
)

