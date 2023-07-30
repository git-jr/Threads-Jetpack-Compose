package com.paradoxo.threadscompose.ui.search

import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
) {
    val accountLists = SampleData().userAccounts
    val listState = rememberLazyListState()

    var showBackIcon by remember {
        mutableStateOf(false)
    }

    var showHeaderTitle by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(showHeaderTitle) {
        if (showHeaderTitle) listState.animateScrollToItem(1) else listState.animateScrollToItem(0)
    }

    val alphaHeaderTitle by remember {
        derivedStateOf {
            val isFirstItem = listState.firstVisibleItemIndex == 0
            val offset = listState.firstVisibleItemScrollOffset

            if (isFirstItem) {
                if (offset > 0) {
                    1 - (offset / 100f).coerceIn(0f, 1f)
                } else {
                    1f
                }
            } else {
                0f
            }
        }
    }

    Scaffold { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .alpha(alphaHeaderTitle)
                ) {
                    Text(
                        text = "Pesquisar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                        ),
                    )
                }
            }

            stickyHeader {
                SearchBar(showBackIcon = showBackIcon,
                    onBackClick = {
                        showHeaderTitle = it
                        showBackIcon = it
                    })
            }

            items(accountLists) { account ->
                AccountItem(account)
            }
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onBackClick: (Boolean) -> Unit = {},
    showBackIcon: Boolean = false,
) {
    var searchEditTextState by remember {
        mutableStateOf("")
    }

    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(showBackIcon) {
        if (!showBackIcon) {
            keyboard?.hide()
        }
        onBackClick(showBackIcon)
    }

    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver

    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            if (isKeyboardOpen) {
                onBackClick(true)
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnGlobalLayoutListener(listener)
            } else {
                view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBackIcon) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "back",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .size(24.dp)
                    .clickable {
                        searchEditTextState = ""
                        onBackClick(false)
                    },
                tint = Color.Gray,
            )
        }

        BasicTextField(
            value = searchEditTextState,
            onValueChange = {
                searchEditTextState = it
            },
            maxLines = 1,
            textStyle = TextStyle.Default.copy(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            decorationBox = { innerValue ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.4f)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "search",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .size(24.dp),
                            tint = Color.Gray.copy(alpha = 0.8f)
                        )

                        Box {
                            if (searchEditTextState.isEmpty()) {
                                Text(
                                    text = "Pesquisar",
                                    color = Color.Gray.copy(alpha = 0.8f),
                                )
                            }
                            innerValue()
                        }
                    }

                    if (searchEditTextState.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "close",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .size(24.dp)
                                .clickable {
                                    searchEditTextState = ""
                                },
                            tint = Color.Gray,
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun AccountItem(account: UserAccount) {
    val dividerColor = Color.Gray.copy(alpha = 0.2f)

    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = account.imageProfileUrl,
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.placeholder_image),
                contentDescription = "avatar",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            Modifier
                .padding(vertical = 8.dp)
                .weight(0.5f)
        ) {

            Text(
                text = account.userName,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = account.name,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    AsyncImage(
                        model = R.drawable.profile_pic_emoji_1,
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                        error = painterResource(id = R.drawable.placeholder_image),
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

                    AsyncImage(
                        model = R.drawable.profile_pic_emoji_3,
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                        error = painterResource(id = R.drawable.placeholder_image),
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
                    text = account.followers.size.toString(),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Seguidores",
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .weight(0.3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val isFollowing by remember {
                mutableStateOf(true)
            }

            val textIsFollowing = if (isFollowing) {
                "Seguindo"
            } else {
                "Seguir"
            }

            val textColorByIsFollowState = if (isFollowing) {
                Color.Gray
            } else {
                Color.Black
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(40))

            ) {
                Text(
                    text = textIsFollowing,
                    fontWeight = FontWeight.Bold,
                    color = textColorByIsFollowState,
                    modifier = Modifier
                        .padding(vertical = 6.dp),
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
fun SearchScreenPreview() {
    ThreadsComposeTheme {
        SearchScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountItemPreview() {
    AccountItem(
        SampleData().userAccounts.first()
    )
}