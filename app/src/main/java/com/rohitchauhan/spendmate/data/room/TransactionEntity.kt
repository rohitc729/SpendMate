package com.rohitchauhan.spendmate.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rohitchauhan.spendmate.domain.model.TransactionType


@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val type: TransactionType,
    val amount: Double,

    val category: String,
    val personName: String? = null,

    val title: String,
    val icon: String,
    val message: String,
    val timeStamp: Long,

    // Group-specific fields
    val perPersonAmount: Double? = null,
    val personCount: Int? = null,

    // Identifies the transaction kind
    val isGroupTransaction: Boolean = false
)