package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onNavigateToHome: () -> Unit = {}
) {
    val loginState by loginViewModel.uiState.collectAsState()
    loginViewModel.login()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (loginState.appState) {
            AppState.Loading -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_colors),
                    contentDescription = "app logo",
                    modifier = Modifier.size(200.dp)
                )
            }

            AppState.LoggedIn -> {
                onNavigateToHome()
            }

            AppState.LoggedOut -> {
                Text(text = "Fazer login")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ThreadsComposeTheme {
        LoginScreen()
    }
}

sealed class AppState {
    object Loading : AppState()
    object LoggedIn : AppState()
    object LoggedOut : AppState()
}


data class LoginState(
    var appState: AppState = AppState.Loading
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun login() {
        viewModelScope.launch {
            delay(2000)
            _uiState.value = LoginState(AppState.LoggedIn)
        }
    }
}