package com.paradoxo.threadscompose.ui.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import com.paradoxo.threadscompose.utils.noRippleClickable
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
                if (loginState.profileInServer) {
                    onAuthComplete(loginState.currentUserName)
                } else {
                    onAuthComplete(null)
                }
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

    val context = LocalContext.current

    val facebookAuthLauncher = rememberLauncherForActivityResult(
        contract = loginManager.createLogInActivityResultContract(callbackManager, null),
        onResult = {
            // The result are handled by the callbackManager
        }
    )

    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val data = it.data
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    onAuthComplete(account?.displayName)

                } else {
                    Log.e("googleAuth", "Erro ao logar com o Google", it.exception)
                    onAuthError()
                }
            }
        }
    )

    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(stringResource(R.string.default_web_client_id))
            .requestEmail()
            .build()

    val client = GoogleSignIn.getClient(context, gso)

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
                    .noRippleClickable {
                        facebookAuthLauncher.launch(listOf("email", "public_profile"))
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
                        text = "Entrar com Facebook",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                    )
                    Text(
                        text = "Fazer login",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "logo instagram",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color(66, 133, 244, 255), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .noRippleClickable {
                        googleAuthLauncher.launch(client.signInIntent)
                    }
            ) {
                Box(
                    Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/COxitqgJr1sJnIDe8-jiKhxDx1FrYbtRHKJ9z_hELisAlapwE9LUPh6fcXIfb5vwpbMl4xl9H9TRFPc5NOO8Sb3VSgIBrfRYvW6cUA",
                        contentDescription = "Logo do Google",
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                    Text(
                        text = "Entrar com o Google",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Entrar como convidado",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onAuthComplete(null)
                    }
                    .padding(horizontal = 16.dp, vertical = 22.dp)
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
