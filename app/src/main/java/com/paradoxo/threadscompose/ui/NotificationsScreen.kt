package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Notification
import com.paradoxo.threadscompose.model.NotificationType
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {

    val tabItems = listOf(
        "Tudo",
        "Respostas",
        "Menções",
        "Verificado",
    )

    val state by remember { mutableStateOf(NotificationScreenState()) }
    state.notifications.addAll(SampleData().notifications)

    val allNotifications = state.notifications
    val notifications = allNotifications.toMutableStateList()

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Atividade",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                            ),
                        )
                    })
                NotificationTabs(
                    tabItems = tabItems,
                    onItemSelected = { selectedItem ->
                        if (selectedItem == 0) {
                            notifications.clear()
                            notifications.addAll(allNotifications)
                            return@NotificationTabs
                        } else {
                            val filterList = allNotifications.filter {
                                it.type == NotificationType.values()[selectedItem]
                            }
                            notifications.clear()
                            notifications.addAll(filterList)
                        }
                    }
                )
            }
        },
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValues = paddingValues)

        ) {
            if (notifications.isNotEmpty()) {

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    items(notifications) { notification ->
                        NotificationItem(
                            notification = notification,
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ainda não há nada para ver aqui",
                        color = Color.Black.copy(alpha = 0.4f),
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NotificationTabs(
    tabItems: List<String>,
    onItemSelected: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var selectedItem by remember { mutableStateOf(0) }

    LaunchedEffect(selectedItem) {
        scrollState.animateScrollTo(selectedItem * 100)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Spacer(modifier = Modifier.width(4.dp))
        tabItems.forEachIndexed { index, item ->
            FilterChip(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onItemSelected(selectedItem)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedLabelColor = MaterialTheme.colorScheme.background,
                    selectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
                ),
                shape = RoundedCornerShape(30),
                label = {
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .sizeIn(minWidth = 68.dp)
                            .padding(
                                vertical = 8.dp
                            )
                    )
                },

                )
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
private fun NotificationItem(
    notification: Notification
) {
    val dividerColor = Color.Gray.copy(alpha = 0.2f)
    val iconSpecs = getIconByType(notification.type)

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                Modifier
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Image(
                    painter = painterResource(id = notification.image),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                )

                Box(
                    modifier = Modifier
                        .offset(x = 2.dp, y = 2.dp)
                        .background(
                            Color.White,
                            CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                iconSpecs.second,
                                CircleShape
                            )
                    ) {
                        Icon(
                            painter = iconSpecs.first,
                            contentDescription = "Action",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .padding(5.dp)

                        )
                    }
                }
            }

            Column(
                Modifier
                    .padding(vertical = 8.dp)
                    .weight(0.5f)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = " ${notification.time}",
                        color = Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }

                Text(
                    text = notification.description,
                    color = Color.Black.copy(alpha = 0.4f),
                )
                notification.extraContent?.let {
                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )
                    Text(
                        text = it,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .weight(0.3f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val textIsFollowing = if (notification.isFollowing) {
                    "Seguindo"
                } else {
                    "Seguir"
                }

                val textColorByIsFollowState = if (notification.isFollowing) {
                    Color.Gray
                } else {
                    Color.Black
                }

                if (notification.type == NotificationType.Follow) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {}
                            .fillMaxWidth()
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
        }

        Row {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)

            )
            Divider(
                color = dividerColor,
                modifier = Modifier
                    .weight(0.9f)
                    .height(1.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun getIconByType(type: NotificationType): Pair<Painter, Color> {
    return when (type) {
        NotificationType.Follow -> Pair(
            rememberVectorPainter(Icons.Default.Person),
            Color(0xFF6B3AEE)
        )

        NotificationType.Like -> Pair(
            painterResource(id = R.drawable.ic_heart),
            Color(0xFFFB0169)
        )

        NotificationType.Comment -> Pair(
            painterResource(id = R.drawable.ic_reply),
            Color(0xFF1FC1FC)
        )

        NotificationType.Mention -> Pair(
            painterResource(id = R.drawable.ic_attach_file),
            Color(0xFF18C686)
        )

        else -> Pair(rememberVectorPainter(Icons.Default.Notifications), Color.Black)
    }
}


@Preview(showBackground = true)
@Composable
private fun NotificationItemPreview() {
    NotificationItem(
        SampleData().notifications.first()
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    ThreadsComposeTheme {
        NotificationsScreen()
    }
}


internal data class NotificationScreenState(
    val notifications: MutableList<Notification> = mutableListOf(),
)