package com.paradoxo.threadscompose.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.paradoxo.threadscompose.ui.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToInstagram: () -> Unit = { }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_net),
                            contentDescription = "Profile",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(24.dp),

                            )
                    },
                    actions = {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_insta),
                            contentDescription = "Instagram",
                            tint = Color.Black,
                            modifier = Modifier
                                .clickable {
                                    onNavigateToInstagram()
                                }
                                .padding(8.dp)
                                .size(24.dp),
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu_config),
                            contentDescription = "Profile",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(24.dp)
                        )
                    }
                )
            }
        },
    ) { paddingValues ->

        val postLists = SampleData().posts
        val repliesList = SampleData().posts.shuffled()

        var tabState by remember { mutableStateOf(0) }
        val titles = listOf("Threads", "Respostas")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
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
                                    fontSize = 24.sp
                                ),
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "jr.obom",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 2.dp))

                                Text(
                                    text = "threads.net",
                                    color = Color.Gray.copy(alpha = 0.6f),
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .background(
                                            color = Color.LightGray.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(50)
                                        )
                                        .padding(vertical = 6.dp, horizontal = 8.dp)
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
                                        .padding(2.dp)
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
                                        .padding(2.dp)
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "Editar Perfil",
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .weight(1f),
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
                                    .align(Alignment.CenterVertically)
                                    .weight(0.5f),
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "Compartilhar Perfil",
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .weight(1f),
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
                                    .align(Alignment.CenterVertically)
                                    .weight(0.5f),
                            )
                        }
                    }
                }

                Column {
                    TabRow(
                        selectedTabIndex = tabState,
                        contentColor = Color.Black,
                        containerColor = Color.Transparent,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier
                                    .tabIndicatorOffset(tabPositions[tabState])
                                    .height(2.dp),
                                color = Color.Black
                            )
                        }
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = tabState == index,
                                onClick = { tabState = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (tabState == index) Color.Black else Color.Gray.copy(
                                            alpha = 0.7f
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            if (tabState == 0) {
                items(postLists) { post ->
                    PostItem(post)
                }
            } else {
                items(repliesList) { reply ->
                    PostItem(reply)
                }
            }
        }

    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}