package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paradoxo.threadscompose.formatTimeElapsed
import com.paradoxo.threadscompose.getCurrentTime
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.sampleData.SampleData


@Composable
fun FeedScreen(modifier: Modifier = Modifier) {

    val postLists = SampleData().posts

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(postLists) { post ->
            PostItem(post)
        }
    }
}

@Composable
private fun PostItem(post: Post) {
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
                painter = painterResource(id = post.userAccount.imageProfileUrl),
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
                    text = post.userAccount.name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.8f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTimeElapsed(post.date, getCurrentTime()),
                        fontWeight = FontWeight.Light
                    )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "more"
                    )
                }
            }

            Text(
                text = post.description,
                modifier = Modifier.padding(end = 8.dp)
            )

            val hasMedia = post.medias.isNotEmpty()

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
                        items(post.medias) { media ->
                            Row {
                                Image(
                                    painter = painterResource(id = media),
                                    contentDescription = "avatar",
                                    contentScale = ContentScale.Crop,
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
private fun PostItemPreiew() {
    PostItem(SampleData().posts.first())
}