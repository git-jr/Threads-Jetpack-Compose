package com.paradoxo.threadscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Divider
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

    val postLists = listOf("Post 1", "Post 2", "Post 3", "Post 4", "Post 5")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(postLists) { post ->
            PostItem()
        }
    }
}

@Composable
fun PostItem() {
    val dividerColor = Color.Gray.copy(alpha = 0.2f)

    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "avatar",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
            )

            Divider(
                color = dividerColor,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxHeight()
                    .width(3.dp)
                    .clip(CircleShape)
            )
        }
        Column(
            Modifier
                .padding(vertical = 8.dp)
                .weight(0.8f)
        ) {
            Row(
                Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sheldon Cooper",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.8f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1h",
                        fontWeight = FontWeight.Light
                    )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "more"
                    )
                }
            }

            Text(
                text = "Alguma frase teste aqui para ver como fica o texto no composable",
                modifier = Modifier.padding(end = 8.dp)
            )

            val hasMedia = true
            val sampleList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

            if (hasMedia) {
                Row(
                    modifier = Modifier
                        .height(height = 100.dp)
                        .fillMaxWidth()
                ) {

                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        items(sampleList) {
                            Row {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_background),
                                    contentDescription = "avatar",
                                    modifier = Modifier
                                        .height(100.dp)
                                        .padding(vertical = 8.dp)
                                        .clip(RoundedCornerShape(10))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
            }

            Row(
                Modifier.padding(vertical = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "like"
                )

                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "comment"
                )

                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "retweet"
                )

                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "share"
                )
            }

            // Like, Comment, Share
        }
    }

    Divider(
        color = dividerColor,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PostItemPreiew() {
    PostItem()
}


@Composable
fun SearchScreen(paddingValues: PaddingValues) {

    val accountLists = mutableListOf<String>()

    // write a for loop to create a list of accounts
    for (i in 1..21) {
        accountLists.add("Account $i")
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(accountLists) { account ->
            AccountItem()
        }
    }
}


@Composable
fun AccountItem() {
    val dividerColor = Color.Gray.copy(alpha = 0.2f)

    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "avatar",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            Modifier
                .padding(vertical = 8.dp)
                .weight(0.5f)
        ) {

            Text(
                text = "Shellstrop69",
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Eleonor Shellstrop",
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(22.dp)
                            .offset(x = (-2).dp)
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(22.dp)
                            .offset(x = (-10).dp)
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    )
                }

                Text(
                    text = "4.243",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Seguidores",
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .weight(0.3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val isFollowing by remember {
                mutableStateOf(true)
            }

            val textIsFollowing = if (isFollowing) {
                "Seguindo"
            } else {
                "Seguir"
            }

            val textColorByIsFollowState = if (isFollowing) {
                Color.Gray
            } else {
                Color.Black
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(40))

            ) {
                Text(
                    text = textIsFollowing,
                    fontWeight = FontWeight.Bold,
                    color = textColorByIsFollowState,
                    modifier = Modifier
                        .padding(vertical = 6.dp),
                )
            }

//            OutlinedButton(
//                onClick = { /*TODO*/ },
//                shape = RoundedCornerShape(40),
//                colors = ButtonDefaults.outlinedButtonColors(
//                    containerColor = Color.White,
//                    contentColor = textColorByIsFollowState
//                ),
//            ) {
//                Text(text = textIsFollowing)
//            }
        }
    }

    Divider(
        color = dividerColor,
        modifier = Modifier
            .fillMaxWidth()
    )
}


@Preview(showBackground = true)
@Composable
fun AccountItemPreiew() {
    AccountItem()
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