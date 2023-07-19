package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.sampleData.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_circle),
                            contentDescription = "Profile",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Profile",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                )
            }
        },
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValues = paddingValues)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.weight(0.8f)) {
                    Text(
                        text = "Junior Martins",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "jr.obom",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        SuggestionChip(
                            onClick = { /*TODO*/ },
                            label = {
                                Text(
                                    text = "threads.net",
                                    fontSize = 12.sp,
                                )
                            },
                            shape = RoundedCornerShape(50),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.LightGray.copy(alpha = 0.3f),
                                labelColor = Color.Gray.copy(alpha = 0.6f)
                            ),
                            border = null,
                        )
                    }
                }

                Box(modifier = Modifier.weight(0.2f)) {

                    Image(
                        painter = painterResource(id = R.drawable.profile_pic_emoji_2),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                Text(
                    text = "Dev Android, produtor de conteudo tech e um Paradoxo!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.profile_pic_emoji_1),
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
                            painter = painterResource(id = R.drawable.profile_pic_emoji_3),
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
                        text = "42",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Seguidores",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "• SemInscreveAí.com",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestionChip(
                        onClick = { /*TODO*/ },
                        label = {
                            Text(
                                text = "Editar perfil",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                fontSize = 16.sp
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color.Transparent,
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            borderColor = Color.Black.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(30),
                        modifier = Modifier
                            .weight(0.5f)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    SuggestionChip(
                        onClick = { /*TODO*/ },
                        label = {
                            Text(
                                text = "Compartilhar perfil",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                fontSize = 16.sp
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color.Transparent,
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            borderColor = Color.Black.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(30),
                        modifier = Modifier.weight(0.5f)
                    )

                }
            }
            TabsContentType()
        }
    }

}

@Composable
private fun TabsContentType() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("Threads", "Respostas")
    Column {
        TabRow(
            selectedTabIndex = state,
            contentColor = Color.Black,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[state])
                        .height(2.dp),
                    color = Color.Black
                )
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state == index) Color.Black else Color.Gray.copy(
                                alpha = 0.7f
                            )
                        )
                    }
                )
            }
        }

        if (state == 0) {
            ThreadsTab()
        } else {
            RepliesTab()
        }
    }
}

@Composable
fun RepliesTab() {
    val postLists = SampleData().posts
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(postLists) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun ThreadsTab() {

    val postLists = SampleData().posts.shuffled()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(postLists) { post ->
            PostItem(post)
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}