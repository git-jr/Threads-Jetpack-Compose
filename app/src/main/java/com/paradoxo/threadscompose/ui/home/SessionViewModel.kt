package com.paradoxo.threadscompose.ui.home

import androidx.lifecycle.ViewModel
import com.paradoxo.threadscompose.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    fun setCurrentUser(currentUser: UserAccount) {
        _uiState.value = _uiState.value.copy(userAccount = currentUser)
    }
}
