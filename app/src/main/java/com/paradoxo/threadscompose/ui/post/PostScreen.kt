package com.paradoxo.threadscompose.ui.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.sampleData.SampleData

@Composable
internal fun PostScreen(
    modifier: Modifier = Modifier,
    currentUser: UserAccount,
    onSendPost: (List<Post>) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val posts = mutableListOf(
        PostScreenState(
            currentUser,
            content = "",
            date = "",
            medias = mutableListOf(),
            isFirstPost = true
        )
    ).toMutableStateList()

    var canAddNewPost by remember {
        mutableStateOf(false)
    }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris: List<Uri> ->

            posts[posts.lastIndex] = posts.last().copy(
                medias = uris.map { it.toString() }.toMutableList()
            )
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            PostScreenAppBar(onBack = onBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    paddingValues = PaddingValues(
                        top = paddingValues.calculateTopPadding(),
                    )
                )
        ) {
            Divider(Modifier.fillMaxWidth(), color = Color.Gray.copy(alpha = 0.2f))

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
                        onRemovePost = {
                            posts.remove(post)
                        },
                        onCanAddNew = {
                            canAddNewPost = it
                        },
                        onSelectMedia = {
                            pickMedia.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        onRemoveMedia = { mediaUri, postaState ->
                            val indexPost = posts.indexOf(postaState)
                            posts[indexPost] = postaState.copy(
                                medias = postaState.medias.filter { it != mediaUri }.toMutableList()
                            )
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
                        AsyncImage(
                            model = currentUser.imageProfileUrl,
                            placeholder = painterResource(
                                id =
                                R.drawable.placeholder_image
                            ),
                            error = painterResource(id = R.drawable.placeholder_image),
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

            Row(
                Modifier
                    .imePadding()
                    .safeGesturesPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Qualquer pessoa pode responder",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray
                    )
                )

                Text(
                    text = "Publicar",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0195F7).copy(
                            alpha = if (canAddNewPost) 1f else 0.5f
                        )
                    ),
                    modifier = Modifier
                        .clickable(enabled = canAddNewPost) {
                            onSendPost(posts.map { it.toPost() })
                        }
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                )
            }

        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostScreenAppBar(
    onBack: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            Row {
                IconButton(onClick = { onBack() }) {
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


@Composable
private fun EditPostItem(
    postState: PostScreenState,
    onAddNew: () -> Unit = {},
    onRemovePost: () -> Unit = {},
    onCanAddNew: (Boolean) -> Unit = {},
    onSelectMedia: (PostScreenState) -> Unit = {},
    onRemoveMedia: (String, PostScreenState) -> Unit = { _, _ -> }
) {
    val focusRequester = remember { FocusRequester() }
    val dividerColor = Color.Gray.copy(alpha = 0.2f)

    var editTextPostState by remember {
        mutableStateOf(postState.content)
    }

    LaunchedEffect(editTextPostState) {
        postState.content = editTextPostState
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
            AsyncImage(
                model = postState.userAccount.imageProfileUrl,
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.placeholder_image),
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
                                onRemovePost()
                            }
                    )
                }
            }


            BasicTextField(
                value = editTextPostState,
                onValueChange = {
                    if (it.endsWith("\n")) {
                        if (isFirstPost && editTextPostState.isEmpty()) {
                            editTextPostState = ""
                            return@BasicTextField
                        } else {
                            if (!isFirstPost && it.endsWith("\n\n\n") && it.length == 3) {
                                return@BasicTextField
                            }
                            if (it.endsWith("\n\n\n") && editTextPostState.isNotEmpty()) {
                                editTextPostState = editTextPostState.dropLast(2)
                                onAddNew()
                                return@BasicTextField
                            }
                        }
                    }
                    if (!it.endsWith("\n\n\n")) {
                        editTextPostState = it
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
                        if (editTextPostState.isEmpty()) {
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


            val size = if (postState.medias.size > 0) 200.dp else 24.dp

            if (postState.medias.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                val scrollState = rememberScrollState()
                Row(
                    Modifier
                        .height(height = size)
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    postState.medias.forEach { imageUri ->
                        Box {
                            AsyncImage(
                                model = imageUri,
                                placeholder = painterResource(id = R.drawable.placeholder_image),
                                error = painterResource(id = R.drawable.placeholder_image),
                                contentDescription = "image",
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(10))
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(10)
                                    )
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(end = 12.dp, top = 22.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        onRemoveMedia(imageUri, postState)
                                    },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "attach file",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            } else {
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
                            .clickable {
                                onSelectMedia(postState)
                            }

                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditPostItemWithImagesPreview() {
    EditPostItem(
        postState = PostScreenState(
            userAccount = SampleData().userAccounts[0],
            content = "",
            date = "10/10/2021",
            medias = mutableListOf("1", "2", "3")
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
    PostScreen(
        currentUser = SampleData().generateSampleInvitedUser()
    )
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
