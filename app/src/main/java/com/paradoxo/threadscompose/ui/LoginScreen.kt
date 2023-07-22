package com.paradoxo.threadscompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    ) {
        when (loginState.appState) {
            AppState.Loading -> {
                SplashScreen()
            }

            AppState.LoggedIn -> {
                onNavigateToHome()
            }

            AppState.LoggedOut -> {
                LoggedOutScreen() {
                    onNavigateToHome()
                }
            }
        }
    }
}

@Composable
fun LoggedOutScreen(
    onNavigateToHome: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_lines_1),
            contentDescription = "app logo"
        )

        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(25)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(25)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Entrar com Instagram",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                    )
                    Text(
                        text = "jr.obom",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_insta),
                    contentDescription = "logo instagram",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Entrar como convidado",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .clickable {
                        onNavigateToHome()
                    }
                    .padding(16.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoggedOutScreenPreview() {
    ThreadsComposeTheme {
        LoggedOutScreen()
    }
}

@Composable
private fun SplashScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_colors),
            contentDescription = "app logo",
            modifier = Modifier.size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ThreadsComposeTheme {
        SplashScreen()
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
            delay(500)
            _uiState.value = LoginState(AppState.LoggedOut)
        }
    }
}