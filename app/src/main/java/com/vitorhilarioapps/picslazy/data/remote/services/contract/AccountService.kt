package com.vitorhilarioapps.picslazy.data.remote.services.contract

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.vitorhilarioapps.picslazy.data.remote.model.TaskResult

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: FirebaseUser?

    suspend fun authenticate(email: String, password: String): TaskResult
    suspend fun sendVerificationEmail(): TaskResult
    suspend fun sendRecoveryEmail(email: String): TaskResult
    suspend fun createAnonymousAccount()
    suspend fun createAccount(name: String, email: String, password: String, avatar: Uri?): TaskResult
    suspend fun deleteAccount(): TaskResult
    suspend fun signOut()
}