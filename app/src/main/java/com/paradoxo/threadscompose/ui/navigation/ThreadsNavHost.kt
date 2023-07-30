package com.paradoxo.threadscompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.paradoxo.threadscompose.ui.home.SessionViewModel

@Composable
fun ThreadsNavHost(
    navController: NavHostController,
    navigateToInstagram: () -> Unit = {}
) {
    val sessionViewModel: SessionViewModel = viewModel()
    val state = sessionViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Destinations.LoginNavigation.route
    ) {
        loginGraph(
            onNavigateToHome = { currentUser ->
                sessionViewModel.setCurrentUser(currentUser)

                navController.navigate(Destinations.HomeNavigation.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onNavigateToProfileEdit = {
                navController.navigate(Destinations.ProfileEdit.route) {
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

