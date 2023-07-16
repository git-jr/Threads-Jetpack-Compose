package com.paradoxo.threadscompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paradoxo.threadscompose.ui.FeedScreen
import com.paradoxo.threadscompose.ui.NotificationsScreen
import com.paradoxo.threadscompose.ui.PostScreen
import com.paradoxo.threadscompose.ui.ProfileScreen
import com.paradoxo.threadscompose.ui.SearchScreen
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import java.time.LocalDateTime
import java.time.ZoneOffset


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val before = getCurrentTime() - (60 * 60 * 23)
        val after = getCurrentTime()
        Log.i("MainActivity", "23 Horas atrás: ${formatTimeElapsed(before, after)}")
        Log.i(
            "MainActivity",
            "3 Dias atrás: ${formatTimeElapsed(before - (60 * 60 * 23 * 3), after)}"
        )
        Log.i(
            "MainActivity",
            "2 Semanas: ${formatTimeElapsed(before - (60 * 60 * 23 * 16), after)}"
        )
        Log.i("MainActivity", "3 Meses: ${formatTimeElapsed(before - (60 * 60 * 23 * 100), after)}")

        setContent {
            ThreadsComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeNavigation()
                }
            }
        }
    }
}


@Composable
fun HomeNavigation() {

    var selectedItem by remember { mutableStateOf(2) }
    val items = listOf("Feed", "Search", "Post", "Notifications", "Profile")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Send,
        Icons.Default.Favorite,
        Icons.Default.Person
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                icons[index], contentDescription = item
                            )
                        },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (selectedItem) {
                0 -> FeedScreen()
                1 -> SearchScreen()
                2 -> PostScreen()
                3 -> NotificationsScreen()
                4 -> ProfileScreen()
            }
        }
    }
}


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
