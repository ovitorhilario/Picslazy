package com.vitorhilarioapps.picslazy.domain.use_case.permissions

import com.vitorhilarioapps.picslazy.domain.repository.AddPhotoRepository
import com.vitorhilarioapps.picslazy.domain.repository.PermissionsRepository
import javax.inject.Inject

class HasPermissionsUseCase @Inject constructor(
    private val repository: PermissionsRepository
) {
    operator fun invoke(): Boolean {
        return repository.hasPermissions()
    }
}