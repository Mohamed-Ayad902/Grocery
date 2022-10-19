package com.example.grocery.di

import android.content.Context
import androidx.room.Room
import com.example.grocery.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFavoriteDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, Database::class.java, "favorite_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: Database) = db.favoriteDao()

    @Singleton
    @Provides
    fun provideCartDao(db: Database) = db.cartDao()

}