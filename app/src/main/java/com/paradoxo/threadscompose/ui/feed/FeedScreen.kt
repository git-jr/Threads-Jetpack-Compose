package com.paradoxo.threadscompose.ui.feed

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.ui.PostItem
import kotlinx.coroutines.launch


@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    posts: List<Post> = emptyList(),
    onLikeClick: (Post) -> Unit = {},
    idCurrentUserProfile: String = "",
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            ExpandableAppLogoLottie()
        }

        items(
            posts,
            key = { post -> post.id }
        ) { post ->
            val isLiked = rememberSaveable {
                mutableStateOf(post.likes.any { it.id == idCurrentUserProfile })
            }
            PostItem(
                post,
                isLiked.value,
                onLikeClick = {
                    onLikeClick(post)
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
private fun ExpandableAppLogoLottie() {
    val maxHeightImage = 200.dp
    val defaultSizeImage = 72.dp
    val coroutineScope = rememberCoroutineScope()
    val animatedImageSize = remember { Animatable(defaultSizeImage.value) }


    val lottiePreviousProgress by remember { mutableFloatStateOf(0f) }
    val lottieSpeed by remember { mutableFloatStateOf(1f) }
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.logo_lines_animated
        )
    )
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(lottieSpeed) {
        lottieAnimatable.animate(
            lottieComposition,
            iteration = LottieConstants.IterateForever,
            speed = lottieSpeed,
        )
    }

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
                        coroutineScope.launch {
                            lottieAnimatable.animate(
                                lottieComposition,
                                iteration = 1,
                                speed = lottieSpeed,
                                reverseOnRepeat = true,
                                initialProgress = lottiePreviousProgress,
                            )
                        }
                    },
                    onDragStart = {
                        coroutineScope.launch {
                            lottieAnimatable.animate(
                                lottieComposition,
                                iteration = LottieConstants.IterateForever,
                                speed = lottieSpeed,
                                initialProgress = lottiePreviousProgress,
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

            LottieAnimation(
                composition = lottieComposition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .size(animatedImageSize.value.dp)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}