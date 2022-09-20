package com.example.grocery.di

import android.content.Context
import androidx.room.Room
import com.example.grocery.db.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavoriteDatabaseModule {

    @Singleton
    @Provides
    fun provideFavoriteDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDao(db: FavoriteDatabase) = db.dao()

}