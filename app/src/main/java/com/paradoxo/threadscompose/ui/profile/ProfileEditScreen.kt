package com.paradoxo.threadscompose.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.UserAccount


@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    userAccount: UserAccount,
    onSave: (UserAccount) -> Unit = {}
) {

    var bioEditTextState by remember {
        mutableStateOf(userAccount.bio)
    }

    var linkEditTextState by remember {
        mutableStateOf(userAccount.link)
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .imePadding()
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Personalize seu perfil no Lines",
                color = Color.Gray.copy(alpha = 0.8f)
            )
        }

        Column {
            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = "Nome",
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "lock",
                                Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 2.dp))

                            Text("${userAccount.name} (${userAccount.userName})")
                        }
                    }

                    AsyncImage(
                        model = userAccount.imageProfileUrl,
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                        error = painterResource(id = R.drawable.placeholder_image),
                        contentDescription = "logo imagem perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp)
                    )
                }
                Row {
                    Divider(
                        color = Color.Gray.copy(alpha = 0.15f),
                        modifier = Modifier.weight(0.8f)
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Text(
                            text = "Bio",
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        BasicTextField(
                            value = bioEditTextState,
                            onValueChange = { bioEditTextState = it },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            textStyle = TextStyle.Default.copy(
                                fontSize = 16.sp
                            ),
                            decorationBox = { innerValue ->
                                Box {
                                    if (bioEditTextState.isEmpty()) {
                                        Text(
                                            text = "+Escrever bio",
                                            color = Color.Gray.copy(alpha = 0.8f)
                                        )
                                    }
                                    innerValue()
                                }
                            },
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.surfaceVariant)
                        )

                    }
                }

                Divider(color = Color.Gray.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Text(
                            text = "Link",
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        BasicTextField(
                            value = linkEditTextState,
                            onValueChange = { linkEditTextState = it },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            textStyle = TextStyle.Default.copy(
                                fontSize = 16.sp
                            ),
                            decorationBox = { innerValue ->
                                Box {
                                    if (linkEditTextState.isEmpty()) {
                                        Text(
                                            text = "+Adicionar link",
                                            color = Color.Gray.copy(alpha = 0.8f)
                                        )
                                    }
                                    innerValue()
                                }
                            },
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.surfaceVariant)
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onSave(
                        userAccount.copy(
                            bio = bioEditTextState,
                            link = linkEditTextState
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Link Start!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditScreenPreview() {
    ProfileEditScreen(
        userAccount = UserAccount(
            name = "Nome",
            bio = "Bio",
            link = "Link",
            imageProfileUrl = "https://bit.ly/43CtVSz"
        )
    )
}