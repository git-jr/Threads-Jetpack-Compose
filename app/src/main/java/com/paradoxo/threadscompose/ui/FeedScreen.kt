package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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


@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}