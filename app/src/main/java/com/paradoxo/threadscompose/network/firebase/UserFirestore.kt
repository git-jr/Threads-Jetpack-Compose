package com.paradoxo.threadscompose.network.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.paradoxo.threadscompose.model.UserAccount

class UserFirestore {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val dbUsers = firebaseFirestore.collection("users")


    fun getUserById(
        userId: String,
        onSuccess: (UserAccount) -> Unit = {},
        onError: () -> Unit = {},
    ) {
        dbUsers.document(userId)
            .get()
            .addOnSuccessListener {
                it.toObject(UserAccount::class.java)?.let { userAccount ->
                    onSuccess(userAccount)
                    Log.i("getUserById", "Usuário obtido com sucesso ${userAccount.name}")
                } ?: run {
                    onError()
                    Log.i("getUserById", "Usuário não encontrado")
                }

            }.addOnFailureListener {
                Log.i("getUserById", "Erro ao obter usuário ${it.message}")
            }
    }


    fun saveUser(
        userId: String,
        userAccount: UserAccount,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        dbUsers.document(userId)
            .set(userAccount)
            .addOnSuccessListener {
                onSuccess()
                Log.i("saveUser", "Usuário salvo com sucesso")
            }
            .addOnFailureListener {
                onError()
                Log.i("saveUser", "Erro ao salvar usuário ${it.message}")
            }
    }
}