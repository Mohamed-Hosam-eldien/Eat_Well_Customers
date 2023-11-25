package com.codingtester.eatwell.model.di

import com.codingtester.eatwell.model.repo.DataRepository
import com.codingtester.eatwell.model.repo.RegisterRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDataRepository(): DataRepository {
        return DataRepository()
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideRegisterRepository(): RegisterRepository {
        return RegisterRepository(FirebaseAuth.getInstance())
    }
}