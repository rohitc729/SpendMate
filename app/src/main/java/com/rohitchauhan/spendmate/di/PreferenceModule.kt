package com.rohitchauhan.spendmate.di

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.tooling.preview.Preview
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("spendmate_prefs", Context.MODE_PRIVATE)
    }
}