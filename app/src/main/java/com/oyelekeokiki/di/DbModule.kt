package com.oyelekeokiki.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oyelekeokiki.database.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {
  @Provides
  @Singleton
  fun provideDb(app: Application) = Room
      .databaseBuilder(app, AppDataBase::class.java, "ClothesStore.db")
      .fallbackToDestructiveMigration()
      .build()

  @Provides
  @Singleton
  fun provideRoomDb(db: AppDataBase): RoomDatabase = db

  @Provides
  @Singleton
  fun provideUserDao(db: AppDataBase) = db.wishListDao()
}
