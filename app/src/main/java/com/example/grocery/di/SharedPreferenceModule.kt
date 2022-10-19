package com.example.grocery.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.grocery.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SharedPreferenceModule {

    @ViewModelScoped
    @Provides
    fun provideSharedPreference(@ApplicationContext app: Context) =
        app.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE)!!

    @ViewModelScoped
    @Provides
    fun provideIsFirstTime(sp: SharedPreferences) =
        sp.getBoolean(Constants.KEY_IS_FIRST_TIME, true)

}