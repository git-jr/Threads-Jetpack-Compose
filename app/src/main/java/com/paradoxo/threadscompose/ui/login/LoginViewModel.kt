package com.paradoxo.threadscompose.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.network.firebase.UserFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random


internal class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = Firebase.auth
    private var userFirestore = UserFirestore()

    init {
        verifyLoginState()
    }

    private fun verifyLoginState() {
        viewModelScope.launch {
            delay(Random.nextLong(600, 1200))
            if (auth.currentUser != null) {
                _uiState.value = _uiState.value.copy(
                    appState = AppState.LoggedIn,
                    profileInServer = true,
                    currentUserName = auth.currentUser?.displayName ?: ""
                )
            } else {
                _uiState.value = LoginState(AppState.LoggedOut)
            }
        }
    }


    fun getProfile(
        onSuccess: (UserAccount) -> Unit = {},
        onError: () -> Unit = {},
    ) {
        val userId = auth.currentUser?.uid
        userId?.let {
            userFirestore.getUserById(
                userId = userId,
                onSuccess = {
                    onSuccess(it)
                },
                onError = onError
            )
        } ?: onError()
    }

    fun saveNewUser(
        userAccount: UserAccount,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        auth.currentUser?.let {
            val user = userAccount.copy(
                id = it.uid,
                name = it.displayName ?: "",
                userName = it.displayName?.replace(" ", "") ?: "",
            )

            userFirestore.saveUser(
                userId = it.uid,
                userAccount = user,
                onSuccess = onSuccess,
                onError = onError
            )
        } ?: onError()
    }

    fun getCurrentUser(): UserAccount {
        val currentUser = Firebase.auth.currentUser
        val profile = Profile.getCurrentProfile()
        val profileFacebookPicUri = profile?.getProfilePictureUri(200, 200).toString()

        val photoUrl = currentUser?.photoUrl.toString().replace("=s96", "=s200")
        val profileImage = profileFacebookPicUri.ifEmpty { photoUrl }

        val userAccount = UserAccount(
            id = currentUser?.uid ?: "",
            name = currentUser?.displayName ?: "",
            userName = currentUser?.displayName?.lowercase()?.replace(" ", "_") ?: "",
            imageProfileUrl = profileImage
        )

        return userAccount
    }
}