package com.vitorhilarioapps.picslazy.data.remote.services.impl

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.vitorhilarioapps.picslazy.data.remote.model.TaskResult
import com.vitorhilarioapps.picslazy.data.remote.model.TaskState
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun authenticate(email: String, password: String): TaskResult {
        return auth.signInWithEmailAndPassword(email, password).response()
    }

    override suspend fun sendVerificationEmail(): TaskResult {
        return auth.currentUser?.sendEmailVerification()?.response() ?: TaskResult.Failure(null)
    }

    override suspend fun sendRecoveryEmail(email: String): TaskResult {
        return auth.sendPasswordResetEmail(email).response()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun createAccount(name: String, email: String, password: String, avatar: Uri?): TaskResult {
        val createUserTask = auth.createUserWithEmailAndPassword(email, password).response()

        val profileUpdates = with(UserProfileChangeRequest.Builder()) {
            displayName = name
            avatar?.let { uri -> photoUri = uri }
            build()
        }

        return when(createUserTask) {
            is TaskResult.Success -> {
                val user = auth.currentUser
                user?.updateProfile(profileUpdates)?.response() ?: TaskResult.Failure(null)
            }
            is TaskResult.Failure -> createUserTask
        }
    }

    override suspend fun deleteAccount(): TaskResult {
        return auth.currentUser!!.delete().response()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()

        //createAnonymousAccount()
    }

    private suspend fun <T> Task<T>.response(): TaskResult {
        await()

        return if(isSuccessful) {
            TaskResult.Success
        } else {
            TaskResult.Failure(this.exception?.cause)
        }
    }
}