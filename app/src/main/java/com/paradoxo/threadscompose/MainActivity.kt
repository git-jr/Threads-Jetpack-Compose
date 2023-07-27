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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.facebook.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.network.firebase.PostFirestore
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.ui.FeedScreen
import com.paradoxo.threadscompose.ui.NotificationsScreen
import com.paradoxo.threadscompose.ui.PostScreen
import com.paradoxo.threadscompose.ui.SearchScreen
import com.paradoxo.threadscompose.ui.login.AppState
import com.paradoxo.threadscompose.ui.login.LoginScreen
import com.paradoxo.threadscompose.ui.login.LoginViewModel
import com.paradoxo.threadscompose.ui.profile.ProfileEditScreen
import com.paradoxo.threadscompose.ui.profile.ProfileScreen
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import com.paradoxo.threadscompose.utils.showMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val testMode = true

        if (testMode) {
            setContent {
                PostScreen(currentUser = SampleData().generateSampleInvitedUser())

                val postFirestore = PostFirestore()
//                postFirestore.getAllPosts(
//                    onSuccess = { posts ->
//                        setContent {
//                            ThreadsComposeTheme {
//                                FeedScreen(posts = posts)
//                            }
//                        }
//                    },
//                    onError = {
//                        setContent {
//                            ThreadsComposeTheme {
//                                FeedScreen(posts = SampleData().posts)
//                            }
//                        }
//                    }
//                )


//                postFirestore.insertPost(
//                    SampleData().posts.first().copy(
//                        medias = listOf(),
//                        comments = listOf(),
//                        likes = listOf()
//                    )
//                )
            }
        } else {
            setContent {
                ThreadsComposeTheme {
                    Box(Modifier.fillMaxSize()) {
                        val navController: NavHostController = rememberNavController()

                        var showNavigationBar by remember { mutableStateOf(false) }
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        val destinysWithoutNavigationBar by remember {
                            mutableStateOf(
                                listOf(
                                    Destiny.Login.route,
                                    Destiny.ProfileEdit.route,
                                    Destiny.Post.route,
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
        navigateToInstagram: () -> Unit = {}
    ) {
        val sessionViewModel: SessionViewModel = viewModel()
        val state = sessionViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Destiny.LoginNavigation.route
        ) {
            loginGraph(
                onNavigateToHome = { currentUser ->
                    sessionViewModel.serCurrent(currentUser)

                    navController.navigate(Destiny.HomeNavigation.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfileEdit = {
                    navController.navigate(Destiny.ProfileEdit.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            homeGraph(
                state = state,
                onNavigateToInstagram = {
                    navigateToInstagram()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }


    private fun NavGraphBuilder.loginGraph(
        onNavigateToHome: (UserAccount) -> Unit = {},
        onNavigateToProfileEdit: () -> Unit = {}
    ) {
        navigation(
            startDestination = Destiny.Login.route,
            route = Destiny.LoginNavigation.route
        ) {
            composable(Destiny.Login.route) {
                val loginViewModel: LoginViewModel = viewModel()

                LoginScreen(
                    loginViewModel = loginViewModel,
                    onAuthComplete = {
                        it?.let {
                            loginViewModel.getProfile(
                                onSuccess = { userAccountOnFirebase ->
                                    if (userAccountOnFirebase.userName.isNotEmpty()) {
                                        onNavigateToHome(userAccountOnFirebase)
                                    } else {
                                        onNavigateToProfileEdit()
                                    }
                                },
                                onError = {
                                    onNavigateToProfileEdit()
                                }
                            )
                        } ?: run {
                            onNavigateToHome(SampleData().generateSampleInvitedUser())
                        }
                    },
                )
            }

            composable(Destiny.ProfileEdit.route) {
                val loginViewModel: LoginViewModel = viewModel()
                val context = LocalContext.current

                val currentUser = Firebase.auth.currentUser

                val profile = Profile.getCurrentProfile()
                val profilePicUri = profile?.getProfilePictureUri(200, 200)

                val userAccount = UserAccount(
                    id = currentUser?.uid ?: "",
                    name = currentUser?.displayName ?: "",
                    userName = currentUser?.displayName?.lowercase()?.replace(" ", "_") ?: "",
                    imageProfileUrl = profilePicUri?.toString() ?: "",
                )

                ProfileEditScreen(
                    userAccount = userAccount,
                    onSave = { userAccountUpdated ->
                        loginViewModel.saveNewUser(
                            userAccount = userAccountUpdated,
                            onSuccess = {
                                onNavigateToHome(userAccountUpdated)
                            },
                            onError = {
                                context.showMessage("Erro ao salvar informações do perfil")
                            }
                        )
                    }
                )
            }
        }
    }

    private fun NavGraphBuilder.homeGraph(
        state: State<SessionState>,
        onNavigateToInstagram: () -> Unit = {},
        onBack: () -> Unit = {},
        paddingValues: PaddingValues = PaddingValues(0.dp)

    ) {
        navigation(
            startDestination = Destiny.Feed.route,
            route = Destiny.HomeNavigation.route
        ) {
            composable(Destiny.Feed.route) {
                val postViewModel: FeedViewModel = viewModel()
                val postState by postViewModel.uiState.collectAsState()

                FeedScreen(posts = postState.posts)
            }
            composable(Destiny.Search.route) { SearchScreen() }
            composable(Destiny.Post.route) {
                PostScreen(
                    currentUser = state.value.userAccount,
                    onBack = {
                        onBack()
                    },
                    onSendPost = { posts ->
                        val postFirestore = PostFirestore()
                        postFirestore.insertPost(
                            posts = posts,
                            onSuccess = {
                                onBack()
                            },
                            onError = {
                                onBack()
                            }
                        )
                    }
                )
            }
            composable(Destiny.Notifications.route) { NotificationsScreen() }
            composable(Destiny.Profile.route) {
                ProfileScreen(
                    currentUser = state.value.userAccount,
                    modifier = Modifier.padding(paddingValues),
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
        object ProfileEdit : Destiny("profileEdit")

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

internal class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedScreenState())
    val uiState: StateFlow<FeedScreenState> = _uiState.asStateFlow()

    init {
        val postFirestore = PostFirestore()
        postFirestore.getAllPosts(
            onSuccess = { posts ->
                _uiState.value = _uiState.value.copy(posts = posts)
            },
            onError = {
                _uiState.value = _uiState.value.copy(posts = SampleData().posts)
            }
        )
    }
}

data class FeedScreenState(
    var posts: List<Post> = emptyList(),
)

internal class SessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    fun serCurrent(currentUser: UserAccount) {
        _uiState.value = _uiState.value.copy(userAccount = currentUser)
    }
}

data class SessionState(
    var appState: AppState = AppState.Loading,
    var userAccount: UserAccount = UserAccount(),
)