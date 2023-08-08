package com.vitorhilarioapps.picslazy.common.model

sealed class Events {
    data class Info(val info: String): Events()
    data class Error(val error: String): Events()
    data class Success(val message: String): Events()
}