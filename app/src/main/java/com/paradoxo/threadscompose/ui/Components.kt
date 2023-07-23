package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
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
import coil.compose.AsyncImage
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.utils.formatTimeElapsed
import com.paradoxo.threadscompose.utils.getCurrentTime

@Composable
fun PostItem(post: Post) {
    val dividerColor = Color.Gray.copy(alpha = 0.2f)

    val commentsSize = post.comments.size
    val likesSize = post.likes.size
    val groupImageOffsetSize = if (commentsSize > 0) 8.dp else 0.dp
    val hasMedia = post.medias.isNotEmpty()

    Column {
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
                AsyncImage(
                    model = post.userAccount.imageProfileUrl,
                    placeholder = painterResource(id = R.drawable.placeholder_image),
                    error = painterResource(id = R.drawable.placeholder_image),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(CircleShape)
                )

                if (commentsSize > 0 || likesSize > 0) {
                    Divider(
                        color = dividerColor,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxHeight()
                            .width(3.dp)
                            .clip(CircleShape)
                    )
                }
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
                                    AsyncImage(
                                        model = media,
                                        placeholder = painterResource(id = R.drawable.placeholder_image),
                                        error = painterResource(id = R.drawable.placeholder_image),
                                        contentDescription = "avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .clip(RoundedCornerShape(10))
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(10)
                                            )

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
        if (commentsSize > 0 || likesSize > 0) {
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier
                        .weight(0.2f)
                        .padding(start = 8.dp)
                        .offset(y = (-groupImageOffsetSize * 2)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    when (post.comments.size) {
                        1 -> {
                            ContainerOneProfilePic()
                        }

                        2 -> {
                            ContainerTwoProfilePics()
                        }

                        else -> {
                            ContainerMoreTwoProfilePics()
                        }
                    }

                }
                Row(
                    Modifier
                        .weight(0.8f)
                        .offset(y = (-groupImageOffsetSize * 2)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (commentsSize > 0) {
                        val textCommentsSize =
                            if (commentsSize == 1) "1 Resposta" else "$commentsSize Respostas"
                        Text(
                            text = textCommentsSize,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.Gray.copy(alpha = 0.8f)
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    if (likesSize > 0) {
                        val separator = if (commentsSize > 0) " • " else ""
                        val textLikesSize =
                            separator + if (likesSize == 1) "1 Curtida" else "$likesSize Curtidas"

                        Text(
                            text = textLikesSize,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.Gray.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }
    }

    Divider(
        color = dividerColor,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = -groupImageOffsetSize * 2)

    )
}


@Composable
private fun ContainerOneProfilePic() {
    Box(
        Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_pic_emoji_3),
            contentDescription = "avatar",
            modifier = Modifier
                .size(22.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(2.dp)
        )
    }
}

@Composable
private fun ContainerTwoProfilePics() {
    Box(
        Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_pic_emoji_3),
            contentDescription = "avatar",
            modifier = Modifier
                .size(22.dp)
                .offset(x = (-5).dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(2.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.profile_pic_emoji_1),
            contentDescription = "avatar",
            modifier = Modifier
                .size(22.dp)
                .offset(x = 5.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(2.dp)
        )
    }
}

@Composable
private fun ContainerMoreTwoProfilePics() {
    Box(
        Modifier.padding(horizontal = 8.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_pic_emoji_3),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(22.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .padding(2.dp)
                    .align(Alignment.End),
            )

            Image(
                painter = painterResource(id = R.drawable.profile_pic_emoji_1),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = (-14).dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .padding(2.dp)
                    .align(Alignment.Start)
            )

            Image(
                painter = painterResource(id = R.drawable.profile_pic_emoji_4),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(14.dp)
                    .offset(y = (-18).dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .padding(2.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostItemPreview() {
    PostItem(SampleData().posts.first())
}