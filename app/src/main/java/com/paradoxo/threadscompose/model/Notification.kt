package com.paradoxo.threadscompose.model


enum class NotificationType {
    All,
    Comment,
    Mention,
    Follow,
    Like,
    Verified,
}


data class Notification(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val image: Int = 0,
    val time: String = "",
    val extraContent: String? = null,
    val type: NotificationType = NotificationType.Follow,
    var isFollowing: Boolean = false,
)
