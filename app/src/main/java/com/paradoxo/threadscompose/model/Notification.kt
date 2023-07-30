package com.paradoxo.threadscompose.model

data class Notification(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val image: Int = 0,
    val time: String = "",
    val extraContent: String? = null,
    val type: NotificationTypeEnum = NotificationTypeEnum.Follow,
    var isFollowing: Boolean = false,
)

enum class NotificationTypeEnum {
    All,
    Comment,
    Mention,
    Follow,
    Like,
    Verified,
}
