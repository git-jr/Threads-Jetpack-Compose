package com.paradoxo.threadscompose.ui.login

sealed class AppState {
    object Loading : AppState()
    object LoggedIn : AppState()
    object LoggedOut : AppState()
}

data class LoginState(
    var appState: AppState = AppState.Loading,
    var profileInServer: Boolean = false,
    val currentUserName: String = "",
)