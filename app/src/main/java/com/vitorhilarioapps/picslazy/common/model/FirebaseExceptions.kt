package com.vitorhilarioapps.picslazy.common.model

sealed class FirebaseExceptions {
    object InvalidCredentials: FirebaseExceptions()
    object InvalidUser: FirebaseExceptions()
    object Invalid: FirebaseExceptions()
}