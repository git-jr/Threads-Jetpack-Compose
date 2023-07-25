package com.paradoxo.threadscompose.ui.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import com.paradoxo.threadscompose.utils.showMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
internal fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onAuthComplete: (String?) -> Unit = {},
) {
    val loginState by loginViewModel.uiState.collectAsState()

    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
    ) {
        when (loginState.appState) {
            AppState.Loading -> {
                SplashScreen()
            }

            AppState.LoggedIn -> {
                onAuthComplete(null)
            }

            AppState.LoggedOut -> {
                LoggedOutScreen(
                    onAuthComplete = { profileName ->
                        if (profileName == null) {
                            onAuthComplete(null)
                        } else {
                            onAuthComplete(profileName)
                        }
                        Log.i("login", "onAuthComplete: $profileName")
                    },
                    onAuthError = {
                        context.showMessage("Erro ao fazer login, tente novamente")
                    },
                    onAuthCancel = {
                        context.showMessage("Login cancelado")
                    }
                )
            }
        }
    }
}

@Composable
fun LoggedOutScreen(
    onAuthComplete: (String?) -> Unit = {},
    onAuthError: () -> Unit = {},
    onAuthCancel: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }

    val launcher = rememberLauncherForActivityResult(
        contract = loginManager.createLogInActivityResultContract(callbackManager, null),
        onResult = {
            // The result are handled by the callbackManager
        }
    )

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                onAuthCancel()
            }

            override fun onError(error: FacebookException) {
                onAuthError()
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    val token = result.accessToken.token
                    val credential = FacebookAuthProvider.getCredential(token)
                    val authResult = Firebase.auth.signInWithCredential(credential).await()
                    if (authResult.user != null) {
                        Profile.getCurrentProfile()?.name?.let { profileName ->
                            onAuthComplete(profileName)
                        } ?: run {
                            Log.e("login", "Login error: Profile name is null")
                            onAuthError()
                        }
                    } else {
                        Log.i("login", "Unable to sign in with Facebook")
                        onAuthError()
                    }
                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }

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
                    .clickable {
                        launcher.launch(listOf("email", "public_profile"))
                    }
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
                        onAuthComplete(null)
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ThreadsComposeTheme {
        LoginScreen()
    }
}
