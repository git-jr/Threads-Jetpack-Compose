package com.paradoxo.threadscompose.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


internal class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = Firebase.auth

    init {
        viewModelScope.launch {
            delay(1500)
            if (auth.currentUser != null) {
                _uiState.value = LoginState(AppState.LoggedIn)
            } else {
                _uiState.value = LoginState(AppState.LoggedOut)
            }
        }
    }
}