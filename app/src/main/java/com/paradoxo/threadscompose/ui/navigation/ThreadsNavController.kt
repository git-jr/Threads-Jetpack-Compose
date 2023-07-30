package com.paradoxo.threadscompose.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.facebook.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.network.firebase.PostFirestore
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.ui.feed.FeedScreen
import com.paradoxo.threadscompose.ui.search.SearchScreen
import com.paradoxo.threadscompose.ui.feed.FeedViewModel
import com.paradoxo.threadscompose.ui.home.SessionState
import com.paradoxo.threadscompose.ui.login.LoginScreen
import com.paradoxo.threadscompose.ui.login.LoginViewModel
import com.paradoxo.threadscompose.ui.notification.NotificationsScreen
import com.paradoxo.threadscompose.ui.post.PostScreen
import com.paradoxo.threadscompose.ui.profile.ProfileEditScreen
import com.paradoxo.threadscompose.ui.profile.ProfileScreen
import com.paradoxo.threadscompose.utils.showMessage
import kotlinx.coroutines.launch

internal fun NavGraphBuilder.loginGraph(
    onNavigateToHome: (UserAccount) -> Unit = {},
    onNavigateToProfileEdit: () -> Unit = {}
) {
    navigation(
        startDestination = Destinations.Login.route,
        route = Destinations.LoginNavigation.route
    ) {
        composable(Destinations.Login.route) {
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

        composable(Destinations.ProfileEdit.route) {
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

internal fun NavGraphBuilder.homeGraph(
    state: State<SessionState>,
    onNavigateToInstagram: () -> Unit = {},
    onBack: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    navigation(
        startDestination = Destinations.Feed.route,
        route = Destinations.HomeNavigation.route
    ) {
        composable(Destinations.Feed.route) {
            val postViewModel: FeedViewModel = viewModel()
            val postState by postViewModel.uiState.collectAsState()

            FeedScreen(posts = postState.posts)
        }
        composable(Destinations.Search.route) { SearchScreen() }
        composable(Destinations.Post.route) {

            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            PostScreen(
                currentUser = state.value.userAccount,
                onBack = {
                    onBack()
                },
                onSendPost = { posts ->
                    context.showMessage("Publicando")
                    val postFirestore = PostFirestore()
                    scope.launch {
                        postFirestore.savePost(
                            posts = posts,
                            onSuccess = {
                                context.showMessage("Publicado com sucesso")
                                onBack()
                            },
                            onError = {
                                context.showMessage("Erro ao publicar")
                                onBack()
                            }
                        )
                    }
                }
            )
        }
        composable(Destinations.Notifications.route) { NotificationsScreen() }
        composable(Destinations.Profile.route) {
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
