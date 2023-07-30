package com.paradoxo.threadscompose.ui.post

import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.utils.getCurrentTime

internal data class PostScreenState(
    val userAccount: UserAccount,
    var content: String,
    var medias: MutableList<String> = mutableListOf(),
    val date: String,
    val isFirstPost: Boolean = false
) {
    fun toPost() = Post(
        userAccount = userAccount,
        date = getCurrentTime(),
        description = content,
        medias = medias,
        likes = emptyList(),
        comments = emptyList(),
    )
}