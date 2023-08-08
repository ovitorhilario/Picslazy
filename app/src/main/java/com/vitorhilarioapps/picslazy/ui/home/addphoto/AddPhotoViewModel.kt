package com.vitorhilarioapps.picslazy.ui.home.addphoto

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorhilarioapps.picslazy.R
import com.vitorhilarioapps.picslazy.common.model.Events
import com.vitorhilarioapps.picslazy.data.local.model.Image
import com.vitorhilarioapps.picslazy.data.remote.model.User
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import com.vitorhilarioapps.picslazy.domain.use_case.addphoto.GetImagesFromStorageUseCase
import com.vitorhilarioapps.picslazy.domain.use_case.permissions.HasPermissionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPhotoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getImagesFromStorageUseCase: GetImagesFromStorageUseCase,
    private val hasPermissionsUseCase: HasPermissionsUseCase,
    private val accountService: AccountService
) : ViewModel() {

    private val _photosState = MutableStateFlow<StorageState>(StorageState.Success(emptyList()))
    val photosState: StateFlow<StorageState> = _photosState

    private val _userState = MutableStateFlow<User>(User())
    val userState: StateFlow<User> = _userState

    init {
        _userState.value = getUserInfo()
        checkStorage()
    }

    private fun getUserInfo(): User {
        val firebaseUser = accountService.currentUser

        return User(
            firebaseUser?.uid ?: context.getString(R.string.invalid_id),
            firebaseUser?.displayName ?: context.getString(R.string.invalid_user_name),
            firebaseUser?.email ?: context.getString(R.string.invalid_email),
            firebaseUser?.photoUrl,
            firebaseUser?.isEmailVerified ?: false,
            firebaseUser?.isAnonymous ?: false
        )
    }

    fun checkStorage() {
        if (hasPermissions()) {
            getImagesFromStorage()
        } else {
            viewModelScope.launch {
                _photosState.value = StorageState.PermissionsDenied
            }
        }
    }

    private fun getImagesFromStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getImagesFromStorageUseCase()
                _photosState.value = StorageState.Success(response)
            } catch (e: Exception) {
                _photosState.value = StorageState.Failure(e.cause)
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return hasPermissionsUseCase()
    }

    sealed class StorageState {
        data class Success(val data: List<Image>): StorageState()
        data class Failure(val throwable: Throwable? = null): StorageState()
        object PermissionsDenied: StorageState()
    }
}