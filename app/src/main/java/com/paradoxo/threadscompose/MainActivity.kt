package com.paradoxo.threadscompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.ui.FeedScreen
import com.paradoxo.threadscompose.ui.LoginScreen
import com.paradoxo.threadscompose.ui.NotificationsScreen
import com.paradoxo.threadscompose.ui.PostScreen
import com.paradoxo.threadscompose.ui.ProfileScreen
import com.paradoxo.threadscompose.ui.SearchScreen
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import com.paradoxo.threadscompose.utils.showMessage


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.i("login", "${currentUser.displayName} Está conectado")
        } else {
            Log.i("login", "Não está conectado ao Firebase")
        }

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {
            Log.i("login", "Está conectado ao facebook")
            Log.i(
                "login",
                "Foto de perfil: ${Profile.getCurrentProfile()?.getProfilePictureUri(200, 200)}"
            )

        } else {
            Log.i("login", "Não está conectado ao Facebook")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        auth = Firebase.auth

        setContent {
            ThreadsComposeTheme {
                val context = LocalContext.current
                Box(Modifier.fillMaxSize()) {
                    val navController: NavHostController = rememberNavController()

                    var showNavigationBar by remember { mutableStateOf(false) }
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    LaunchedEffect(currentDestination) {
                        showNavigationBar =
                            !(currentDestination?.route == Destiny.Post.route || currentDestination?.route == Destiny.Login.route)
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
                                },
                                onSayHello = { profileName ->
                                    context.showMessage("Fala aí $profileName!")
                                }
                            )
                        }
                    )
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
                    NavigationBar {
                        screenItems.forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    screen.resourceId?.let { assetIcon ->
                                        Icon(
                                            assetIcon, contentDescription = null
                                        )
                                    }
                                },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    if (screen.route == Destiny.Post.route) {
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

    @Composable
    fun ThreadsNavHost(
        navController: NavHostController,
        navigateToInstagram: () -> Unit = {},
        onSayHello: (String) -> Unit = {}
    ) {
        NavHost(
            navController = navController,
            startDestination = Destiny.LoginNavigation.route
        ) {
            loginGraph(
                onNavigateToHome = { profileName ->
                    navController.navigate(Destiny.HomeNavigation.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                    profileName?.let { onSayHello(it) }
                },
            )
            homeGraph(
                onNavigateToInstagram = {
                    navigateToInstagram()
                }
            )
        }
    }


    private fun NavGraphBuilder.loginGraph(
        onNavigateToHome: (String?) -> Unit = {}
    ) {
        navigation(
            startDestination = Destiny.Login.route,
            route = Destiny.LoginNavigation.route
        ) {
            composable(Destiny.Login.route) {
                LoginScreen(
                    onNavigateToHome = { onNavigateToHome(it) },
                )
            }
        }
    }

    private fun NavGraphBuilder.homeGraph(
        onNavigateToInstagram: () -> Unit = {},
        paddingValues: PaddingValues = PaddingValues(0.dp)

    ) {
        navigation(
            startDestination = Destiny.Feed.route,
            route = Destiny.HomeNavigation.route
        ) {
            composable(Destiny.Feed.route) { FeedScreen() }
            composable(Destiny.Search.route) { SearchScreen() }
            composable(Destiny.Post.route) { PostScreen() }
            composable(Destiny.Notifications.route) { NotificationsScreen() }
            composable(Destiny.Profile.route) {
                ProfileScreen(
                    Modifier.padding(paddingValues),
                    onNavigateToInstagram = {
                        onNavigateToInstagram()
                    }
                )
            }
        }
    }

    sealed class Destiny(val route: String, val resourceId: ImageVector? = null) {
        object Login : Destiny("login", Icons.Default.Person)
        object Feed : Destiny("feed", Icons.Default.Home)
        object Search : Destiny("search", Icons.Default.Search)
        object Post : Destiny("post", Icons.Default.Send)
        object Notifications : Destiny("notifications", Icons.Default.Favorite)
        object Profile : Destiny("profile", Icons.Default.Person)

        object LoginNavigation : Destiny("loginNavigation")
        object HomeNavigation : Destiny("homeNavigation")
    }

    private val screenItems = listOf(
        Destiny.Feed,
        Destiny.Search,
        Destiny.Post,
        Destiny.Notifications,
        Destiny.Profile
    )
}
