package com.paradoxo.threadscompose.ui.notification

import com.paradoxo.threadscompose.model.Notification

internal data class NotificationScreenState(
    val notifications: MutableList<Notification> = mutableListOf(),
)