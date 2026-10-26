package com.rohitchauhan.spendmate.di

import android.content.Context
import androidx.room.Room
import com.rohitchauhan.spendmate.data.room.TransactionDAO
import com.rohitchauhan.spendmate.data.room.TransactionDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = TransactionDatabase::class.java,
            name ="transaction_db"
        ).build()
    }
    @Singleton
    @Provides
    fun provideDao(database: TransactionDatabase): TransactionDAO{
        return database.dao()
    }
}