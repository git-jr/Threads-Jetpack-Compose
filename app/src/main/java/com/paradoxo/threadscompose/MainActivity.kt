package com.paradoxo.threadscompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paradoxo.threadscompose.ui.navigation.Destinations
import com.paradoxo.threadscompose.ui.navigation.ThreadsNavHost
import com.paradoxo.threadscompose.ui.navigation.screenItems
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme

class MainActivity : ComponentActivity() {
    private val testMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            if (testMode) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {}
            } else {
                ThreadsComposeTheme {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        val navController: NavHostController = rememberNavController()

                        var showNavigationBar by remember { mutableStateOf(false) }
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        val destinysWithoutNavigationBar by remember {
                            mutableStateOf(
                                listOf(
                                    Destinations.Login.route,
                                    Destinations.ProfileEdit.route,
                                    Destinations.Post.route,
                                )
                            )
                        }

                        LaunchedEffect(currentDestination) {
                            showNavigationBar =
                                !destinysWithoutNavigationBar.contains(currentDestination?.route)
                        }

                        ThreadsApp(
                            navController = navController,
                            showNavigationBar = showNavigationBar,
                            currentDestination = currentDestination,
                            content = {
                                ThreadsNavHost(
                                    navController = navController,
                                    navigateToInstagram = {
                                        val instagramIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.instagram.com/threadsapp/")
                                        )
                                        startActivity(instagramIntent)
                                    }
                                )
                            }
                        )
                    }
                }
            }

        }
    }

    @Composable
    fun ThreadsApp(
        content: @Composable (PaddingValues) -> Unit,
        navController: NavHostController,
        showNavigationBar: Boolean,
        currentDestination: NavDestination?
    ) {
        Scaffold(
            bottomBar = {
                if (showNavigationBar) {
                    NavigationBar(
                        containerColor = Color.White,
                    ) {
                        screenItems.forEach { screen ->
                            val isSelected =
                                currentDestination?.hierarchy?.any { it.route == screen.route } == true

                            NavigationBarItem(
                                icon = {
                                    screen.resourceId?.let { assetIcon ->
                                        val icon = painterResource(
                                            id = if (isSelected) {
                                                assetIcon.first
                                            } else {
                                                assetIcon.second
                                            },
                                        )


                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .alpha(
                                                    if (isSelected) 1f else 0.5f
                                                )
                                        )
                                    }
                                },
                                selected = isSelected,
                                onClick = {
                                    if (screen.route == Destinations.Post.route) {
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
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.fillMaxSize()) {
                    content(paddingValues)
                }
            }
        }
    }


}