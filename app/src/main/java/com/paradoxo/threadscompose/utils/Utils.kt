package com.paradoxo.threadscompose.utils

import android.content.Context
import android.widget.Toast
import java.time.LocalDateTime
import java.time.ZoneOffset

fun getCurrentTime(): Long {
    return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
}

fun formatTimeElapsed(start: Long, end: Long): String {
    val elapsedSeconds = end - start

    val secondsInMinute = 60
    val secondsInHour = 60 * secondsInMinute
    val secondsInDay = 24 * secondsInHour
    val secondsInWeek = 7 * secondsInDay
    val secondsInMonth = 30 * secondsInDay

    return when {
        elapsedSeconds < secondsInHour -> "${elapsedSeconds / secondsInMinute} min"
        elapsedSeconds < secondsInDay -> "${elapsedSeconds / secondsInHour} h"
        elapsedSeconds < secondsInWeek -> "${elapsedSeconds / secondsInDay} d"
        elapsedSeconds < secondsInMonth -> "${elapsedSeconds / secondsInWeek} sem"
        elapsedSeconds < secondsInMonth * 12 -> "${elapsedSeconds / secondsInMonth} m"
        else -> "${elapsedSeconds / (secondsInMonth * 12)} a"
    }
}


fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}