package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.sampleData.SampleData

@Composable
fun PostScreen(modifier: Modifier = Modifier) {
    val currentUser = SampleData().userAccounts[0]

    Scaffold(
        modifier = modifier,
        topBar = {
            PostScreenAppBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
        ) {
            Divider(Modifier.fillMaxWidth(), color = Color.Gray.copy(alpha = 0.2f))

            val posts = mutableListOf(
                PostScreenState(
                    currentUser, "", "",
                    isFirstPost = true
                )
            ).toMutableStateList()

            var canAddNewPost by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(posts) {
                if (posts.size > 1) {
                    canAddNewPost = true
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(posts) { post ->
                    EditPostItem(
                        postState = post,
                        onAddNew = {
                            posts.add(
                                PostScreenState(
                                    userAccount = currentUser,
                                    content = "",
                                    date = ""
                                )
                            )
                        },
                        onRemove = {
                            posts.remove(post)
                        },
                        onCanAddNew = {
                            canAddNewPost = it
                        }
                    )
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .alpha(
                                if (canAddNewPost) 1f else 0.5f
                            )
                            .clickable(enabled = canAddNewPost) {
                                if (canAddNewPost) {
                                    posts.add(
                                        PostScreenState(
                                            userAccount = currentUser,
                                            content = "",
                                            date = ""
                                        )
                                    )
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = currentUser.imageProfileUrl),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .weight(1.5f)
                                .clip(CircleShape),
                        )

                        Text(
                            "Adicionar à thread...",
                            color = Color.Black.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            ),
                            modifier = Modifier
                                .clip(CircleShape)
                                .weight(8.5f),

                            )
                    }
                }
            }

        }
    }
}


@Composable
private fun EditPostItem(
    postState: PostScreenState,
    onAddNew: () -> Unit = {},
    onRemove: () -> Unit = {},
    onCanAddNew: (Boolean) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val dividerColor = Color.Gray.copy(alpha = 0.2f)

    var editTextState by remember {
        mutableStateOf(postState.content)
    }

    val isFirstPost = postState.isFirstPost

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
                painter = painterResource(id = postState.userAccount.imageProfileUrl),
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
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = postState.userAccount.name,
                    fontWeight = FontWeight.Bold,
                )

                if (!isFirstPost) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onRemove()
                            }
                    )
                }
            }


            BasicTextField(
                value = editTextState,
                onValueChange = {
                    if (it.endsWith("\n")) {
                        if (isFirstPost && editTextState.isEmpty()) {
                            editTextState = ""
                            return@BasicTextField
                        } else {
                            if (!isFirstPost && it.endsWith("\n\n\n") && it.length == 3) {
                                return@BasicTextField
                            }
                            if (it.endsWith("\n\n\n") && editTextState.isNotEmpty()) {
                                editTextState = editTextState.dropLast(2)
                                onAddNew()
                                return@BasicTextField
                            }
                        }
                    }
                    if (!it.endsWith("\n\n\n")) {
                        editTextState = it
                    }

                    if (isFirstPost) {
                        onCanAddNew(it.isNotEmpty())
                    }

                },
                modifier = Modifier
                    .weight(5F)
                    .focusRequester(focusRequester = focusRequester),
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
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp
                                )
                            )
                        }
                        innerValue()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.surfaceVariant)
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_attach_file),
                    contentDescription = "attach file",
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


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostScreenAppBar() {
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

@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
    PostScreen()
}

@Preview(showBackground = true)
@Composable
fun EditPostItemPreview() {
    EditPostItem(
        postState = PostScreenState(
            userAccount = SampleData().userAccounts[0],
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. Donec euismod, nisl eget aliquam ultricies, nisl nisl aliquet nisl, eget aliquam nisl nisl eget nisl. ",
            date = "10/10/2021"
        )
    )
}


internal data class PostScreenState(
    val userAccount: UserAccount,
    val content: String,
    val date: String,
    val isFirstPost: Boolean = false
)
