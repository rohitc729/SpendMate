package com.rohitchauhan.spendmate.domain.model

import androidx.room.PrimaryKey

data class TransactionModel(
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
enum class TransactionType{
    Income,
    Expense
}