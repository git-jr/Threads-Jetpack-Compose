package com.paradoxo.threadscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThreadsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeNavigation()
                    //Greeting("Android")
                }
            }
        }
    }
}


@Composable
fun HomeNavigation() {

    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Search", "Post", "Notifications", "Profile")
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
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            when (selectedItem) {
                0 -> HomeScreen(paddingValues)
                1 -> SearchScreen(paddingValues)
                2 -> PostScreen(paddingValues)
                3 -> NotificationsScreen(paddingValues)
                4 -> ProfileScreen(paddingValues)
            }
        }
    }


}

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    Text(
        text = "Home",
        modifier = Modifier.padding(paddingValues)
    )
}


@Composable
fun SearchScreen(paddingValues: PaddingValues) {
    Text(
        text = "Search",
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
fun PostScreen(paddingValues: PaddingValues) {
    Text(
        text = "Post",
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
fun NotificationsScreen(paddingValues: PaddingValues) {
    Text(
        text = "Notifications",
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
fun ProfileScreen(paddingValues: PaddingValues) {
    Text(
        text = "Profile",
        modifier = Modifier.padding(paddingValues)
    )
}

