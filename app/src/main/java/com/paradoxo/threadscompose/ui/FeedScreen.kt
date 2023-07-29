package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paradoxo.threadscompose.model.Post


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


@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}