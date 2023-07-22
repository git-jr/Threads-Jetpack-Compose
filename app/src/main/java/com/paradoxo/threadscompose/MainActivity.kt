package com.paradoxo.threadscompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paradoxo.threadscompose.ui.FeedScreen
import com.paradoxo.threadscompose.ui.NotificationsScreen
import com.paradoxo.threadscompose.ui.PostScreen
import com.paradoxo.threadscompose.ui.ProfileScreen
import com.paradoxo.threadscompose.ui.SearchScreen
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ThreadsComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController: NavHostController = rememberNavController()

                    HomeNavigation(
                        navController = navController,
                        navigateToInstagram = {
                            val instagramIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://www.instagram.com")
                            }
                            startActivity(instagramIntent)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun HomeNavigation(
    navController: NavHostController,
    navigateToInstagram: () -> Unit = {}
) {
    var showNavigationBar by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(currentDestination) {
        showNavigationBar = currentDestination?.route != Screen.Post.route
    }

    Scaffold(
        bottomBar = {
            if (showNavigationBar) {
                NavigationBar {
                    screenItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    screen.resourceId,
                                    contentDescription = null
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                if (screen.route == Screen.Post.route) {
                                    navController.navigate(screen.route) {
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }


    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            NavHost(navController = navController, startDestination = Screen.Profile.route) {
                composable(Screen.Feed.route) { FeedScreen() }
                composable(Screen.Search.route) { SearchScreen() }
                composable(Screen.Post.route) { PostScreen() }
                composable(Screen.Notifications.route) { NotificationsScreen() }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onNavigateToInstagram = navigateToInstagram
                    )
                }
            }
        }
    }
}


sealed class Screen(val route: String, val resourceId: ImageVector) {
    object Feed : Screen("feed", Icons.Default.Home)
    object Search : Screen("search", Icons.Default.Search)
    object Post : Screen("post", Icons.Default.Send)
    object Notifications : Screen("notifications", Icons.Default.Favorite)
    object Profile : Screen("profile", Icons.Default.Person)
}

val screenItems = listOf(
    Screen.Feed,
    Screen.Search,
    Screen.Post,
    Screen.Notifications,
    Screen.Profile
)