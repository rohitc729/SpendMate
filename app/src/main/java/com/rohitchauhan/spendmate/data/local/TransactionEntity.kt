package com.rohitchauhan.spendmate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rohitchauhan.spendmate.domain.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val personName: String,
    val title: String,
    val icon: String,
    val message: String,
    val timeStamp: Long
)