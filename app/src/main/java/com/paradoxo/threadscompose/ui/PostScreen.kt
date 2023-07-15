package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.sampleData.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
                },
                title = {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nova thread",
                        fontWeight = FontWeight.Bold
                    )
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
        ) {
            Divider(Modifier.fillMaxWidth(), color = Color.Gray.copy(alpha = 0.2f))

            val posts by remember { mutableStateOf(mutableListOf<Post>()) }
            posts.addAll(SampleData().posts.subList(0, 1))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(posts) { post ->
                    EditPostItem(post)
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .alpha(0.5f)
                            .clickable { },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_pic_emoji_1),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .weight(1.5f)
                                .clip(CircleShape)
                        )

                        Text(
                            "Adicionar à thread...",
                            color = Color.Black.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .clip(CircleShape)
                                .weight(8.5f)

                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
    PostScreen()
}

@Composable
private fun EditPostItem(post: Post) {
    val dividerColor = Color.Gray.copy(alpha = 0.2f)
    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                )
                .weight(1.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = post.userAccount.imageProfileUrl),
                contentDescription = "avatar",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(CircleShape)
            )

            Divider(
                color = dividerColor,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxHeight()
                    .width(2.dp)
                    .clip(CircleShape)
            )
        }
        Column(
            Modifier
                .weight(8.5f)
                .padding(vertical = 8.dp)

        ) {
            Text(
                text = post.userAccount.name,
                fontWeight = FontWeight.Bold
            )

            var editTextState by remember {
                mutableStateOf("")
            }

            BasicTextField(
                value = editTextState,
                onValueChange = { editTextState = it },
                modifier = Modifier.weight(5F),
                textStyle = TextStyle.Default.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                decorationBox = { innerValue ->
                    Box {
                        if (editTextState.isEmpty()) {
                            Text(
                                "Iniciar uma thread...",
                                color = Color.Black.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        innerValue()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.surfaceVariant),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_attach_file),
                    contentDescription = "like",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(45f)

                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditPostItemPreiew() {
    EditPostItem(SampleData().posts.first())
}
