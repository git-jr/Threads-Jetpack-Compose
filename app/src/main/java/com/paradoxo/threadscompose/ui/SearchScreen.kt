package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paradoxo.threadscompose.R


@Composable
fun SearchScreen(modifier: Modifier = Modifier) {

    val accountLists = mutableListOf<String>()

    // write a for loop to create a list of accounts
    for (i in 1..21) {
        accountLists.add("Account $i")
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(accountLists) { account ->
            AccountItem()
        }
    }
}


@Composable
private fun AccountItem() {
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
            Image(
                painter = painterResource(id = R.drawable.profile_pic_emoji_2),
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
                text = "Shellstrop69",
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Eleonor Shellstrop",
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
                    Image(
                        painter = painterResource(id = R.drawable.profile_pic_emoji_1),
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
                    )

                    Image(
                        painter = painterResource(id = R.drawable.profile_pic_emoji_3),
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
                    )
                }

                Text(
                    text = "4.243",
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
private fun AccountItemPreiew() {
    AccountItem()
}
