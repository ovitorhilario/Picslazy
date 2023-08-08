package com.vitorhilarioapps.picslazy.data.remote.model

sealed class TaskState {
    object Loading: TaskState()
    object Idle: TaskState()
}