package com.rohitchauhan.spendmate.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [TransactionEntity::class], version = 2, exportSchema = false)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun dao(): TransactionDAO
}