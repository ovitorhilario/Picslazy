package com.vitorhilarioapps.picslazy.di.app

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.vitorhilarioapps.picslazy.data.local.repository.AddPhotoRepositoryImpl
import com.vitorhilarioapps.picslazy.data.local.repository.EditPhotoRepositoryImpl
import com.vitorhilarioapps.picslazy.data.local.repository.PermissionsRepositoryImpl
import com.vitorhilarioapps.picslazy.data.remote.services.contract.AccountService
import com.vitorhilarioapps.picslazy.data.remote.services.impl.AccountServiceImpl
import com.vitorhilarioapps.picslazy.domain.repository.AddPhotoRepository
import com.vitorhilarioapps.picslazy.domain.repository.EditPhotoRepository
import com.vitorhilarioapps.picslazy.domain.repository.PermissionsRepository
import com.vitorhilarioapps.picslazy.common.helpers.FileHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesFileHelper(
        @ApplicationContext context: Context
    ): FileHelper {
        return FileHelper(context)
    }

    @Provides
    fun provideEditPhotoRepository(
        @ApplicationContext context: Context,
        fileHelper: FileHelper
    ): EditPhotoRepository {
        return EditPhotoRepositoryImpl(context, fileHelper)
    }

    @Provides
    fun providesAddPhotoRepository(
        @ApplicationContext context: Context
    ): AddPhotoRepository {
        return AddPhotoRepositoryImpl(context)
    }

    @Provides
    fun providesPermissionsRepository (
        @ApplicationContext context: Context
    ): PermissionsRepository {
        return PermissionsRepositoryImpl(context)
    }
}