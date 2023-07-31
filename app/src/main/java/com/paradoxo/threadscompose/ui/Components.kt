package com.paradoxo.threadscompose.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.utils.formatTimeElapsed
import com.paradoxo.threadscompose.utils.getCurrentTime
import com.paradoxo.threadscompose.utils.noRippleClickable

@Composable
fun PostItem(
    post: Post,
    isLiked: Boolean = false,
    onLikeClick: (String) -> Unit = {},
) {
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
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.userAccount.imageProfileUrl)
                        .crossfade(true)
                        .build(),
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
                            .height(height = 200.dp)
                            .fillMaxWidth()
                    ) {
                        LazyRow(
                            Modifier
                                .fillMaxWidth()
                        ) {
                            items(post.medias) { media ->
                                Row {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(media)
                                            .crossfade(true)
                                            .build(),
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

                    LikeButton(
                        isLiked = isLiked,
                        onLikeClick = onLikeClick,
                        postId = post.id
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painterResource(id = R.drawable.ic_comment),
                        contentDescription = "comment"
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painterResource(id = R.drawable.ic_repost),
                        contentDescription = "retweet"
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painterResource(id = R.drawable.ic_send),
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
                    if (commentsSize > 0) {
                        when (post.comments.size) {
                            1 -> {
                                ContainerOneProfilePic(post.comments.first().profilePicAuthor)
                            }

                            2 -> {
                                val profilePics = post.comments.take(2).map { it.profilePicAuthor }
                                if (profilePics.distinct().size == 1) {
                                    ContainerOneProfilePic(profilePics.first())
                                } else {
                                    ContainerTwoProfilePics(profilePics[0], profilePics[1])
                                }
                            }

                            else -> {
                                val profilePics = post.comments.take(3).map { it.profilePicAuthor }
                                if (profilePics.distinct().size == 1) {
                                    ContainerOneProfilePic(profilePics.first())
                                } else {
                                    ContainerMoreTwoProfilePics(
                                        profilePics[0],
                                        profilePics[1],
                                        profilePics[2]
                                    )
                                }
                            }
                        }
                    } else {
                        when (post.likes.size) {
                            1 -> {
                                ContainerOneProfilePic(post.likes.first().profilePicAuthor)
                            }

                            2 -> {
                                val profilePics = post.likes.take(2).map { it.profilePicAuthor }
                                if (profilePics.distinct().size == 1) {
                                    ContainerOneProfilePic(profilePics.first())
                                } else {
                                    ContainerTwoProfilePics(profilePics[0], profilePics[1])
                                }
                            }

                            else -> {
                                val profilePics = post.likes.take(3).map { it.profilePicAuthor }
                                if (profilePics.distinct().size == 1) {
                                    ContainerOneProfilePic(profilePics.first())
                                } else {
                                    ContainerMoreTwoProfilePics(
                                        profilePics[0],
                                        profilePics[1],
                                        profilePics[2]
                                    )
                                }
                            }
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
                        Spacer(modifier = Modifier.width(4.dp))
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
private fun ContainerOneProfilePic(
    profilePicAuthor: String
) {
    Box(
        Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePicAuthor)
                .crossfade(true)
                .build(),
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
private fun ContainerTwoProfilePics(
    firstProfilePic: String,
    secondProfilePic: String
) {
    Box(
        Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        AsyncImage(
            model = firstProfilePic,
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

        AsyncImage(
            model = secondProfilePic,
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
private fun ContainerMoreTwoProfilePics(
    firstProfilePic: String,
    secondProfilePic: String,
    thirdProfilePic: String
) {
    Box(
        Modifier.padding(horizontal = 8.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = firstProfilePic,
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

            AsyncImage(
                model = secondProfilePic,
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

            AsyncImage(
                model = thirdProfilePic,
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


@Composable
fun LikeButton(
    isLiked: Boolean, onLikeClick: (String) -> Unit,
    postId: String
) {
    val scale: Float by animateFloatAsState(
        targetValue = if (isLiked) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )

    val resourceId = if (isLiked) R.drawable.ic_heart else R.drawable.ic_heart_outlined
    Icon(
        painter = painterResource(id = resourceId),
        contentDescription = "like",
        modifier = Modifier
            .noRippleClickable {
                onLikeClick(postId)
            }
            .scale(scale),
        tint = if (isLiked) Color.Red else Color.Black
    )
}

@Preview(showBackground = true)
@Composable
private fun PostItemPreview() {
    PostItem(
        post = SampleData().posts.first(),
        isLiked = true
    )
}
