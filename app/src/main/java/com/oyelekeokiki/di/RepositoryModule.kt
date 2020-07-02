package com.oyelekeokiki.di
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.networking.RemoteApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindRemoteApi(remoteApiImpl: RemoteApiImpl): RemoteApi
}