package com.paradoxo.threadscompose.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Post
import kotlinx.coroutines.launch


@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    posts: List<Post> = emptyList(),
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            ExpandableAppLogo()
        }

        items(
            posts,
            key = { post -> post.id }
        ) { post ->
            val isLiked = rememberSaveable {
                mutableStateOf(post.likes.contains(post.userAccount.id))
            }
            PostItem(
                post,
                isLiked.value,
                onLikeClick = {
                    isLiked.value = !isLiked.value
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

@Composable
private fun ExpandableAppLogo() {
    val maxHeightImage = 200.dp
    val defaultSizeImage = 72.dp
    val coroutineScope = rememberCoroutineScope()
    val animatedImageSize = remember { Animatable(defaultSizeImage.value) }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, offset ->
                        coroutineScope.launch {
                            val newSize =
                                (animatedImageSize.value + offset / 8).coerceAtLeast(
                                    defaultSizeImage.value
                                )
                            if (newSize < maxHeightImage.value) {
                                animatedImageSize.snapTo(newSize)
                            }
                            change.consume()
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            animatedImageSize.animateTo(
                                defaultSizeImage.value,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_logo_colors),
                contentDescription = null,
                modifier = Modifier
                    .size(animatedImageSize.value.dp)
                    .fillMaxWidth(),
                colorFilter = ColorFilter.tint(color = Color.Gray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}