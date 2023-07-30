package com.paradoxo.threadscompose.ui.home

import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.ui.login.AppState

data class SessionState(
    var appState: AppState = AppState.Loading,
    var userAccount: UserAccount = UserAccount(),
)