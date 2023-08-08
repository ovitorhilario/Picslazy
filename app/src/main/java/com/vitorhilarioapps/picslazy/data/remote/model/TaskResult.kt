package com.vitorhilarioapps.picslazy.data.remote.model

sealed class TaskResult {
    object Success: TaskResult()
    data class Failure(val throwable: Throwable?): TaskResult()
}