package com.rohitchauhan.spendmate.domain.model

data class GroupTransactionModel(
    val id :Int=0,
    val type: TransactionType,
    val amount: Double,
    val perPersonAmount: Double,
    val personCount:Int,
    val category:String,
    val icon: String,
    val title:String,
    val message:String,
    val timeStamp:Long
){
    constructor():this(
        type=TransactionType.Expense,
        amount=0.0,
        perPersonAmount=0.0,
        personCount=1,
        category="",
        icon="",
        title="",
        message="",
        timeStamp=0L
    )
}