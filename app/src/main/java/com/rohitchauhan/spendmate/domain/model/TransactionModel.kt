package com.rohitchauhan.spendmate.domain.model

data class TransactionModel(
    val id :Int,
    val type:TransactionType,
    val amount: Double,
    val category: String,
    val personName:String,
    val title: String,
    val icon: String,
    val message:String,
    val timeStamp: Long
)
enum class TransactionType{
    Income,
    Expense
}