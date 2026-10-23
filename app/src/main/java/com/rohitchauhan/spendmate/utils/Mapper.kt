package com.rohitchauhan.spendmate.utils

import com.rohitchauhan.spendmate.data.local.GroupTransactionEntity
import com.rohitchauhan.spendmate.data.local.TransactionEntity
import com.rohitchauhan.spendmate.domain.model.GroupTransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionModel

fun TransactionModel.toTransactionEntity():TransactionEntity{
    return TransactionEntity(
        id = this.id,
        type = this.type,
        amount = this.amount,
        category = this.category,
        personName = this.personName,
        title = this.title,
        message = this.message,
        timeStamp = this.timeStamp,
        icon = this.icon
    )
}
fun TransactionEntity.toTransactionModel():TransactionModel{
    return TransactionModel(
        id = this.id,
        type = this.type,
        amount = this.amount,
        category = this.category,
        personName = this.personName,
        title = this.title,
        message = this.message,
        timeStamp = this.timeStamp,
        icon = this.icon
    )
}

fun GroupTransactionModel.toGroupTransactionEntity(): GroupTransactionEntity{
    return GroupTransactionEntity(
        id = this.id,
        type = this.type,
        amount = this.amount,
        perPersonAmount = this.perPersonAmount,
        personCount = this.personCount,
        category = this.category,
        icon = this.icon,
        title = this.title,
        message = this.message,
        timeStamp = this.timeStamp,
    )
}
fun GroupTransactionEntity.toGroupTransactionModel(): GroupTransactionModel{
    return GroupTransactionModel(
        id = this.id,
        type = this.type,
        amount = this.amount,
        perPersonAmount = this.perPersonAmount,
        personCount = this.personCount,
        category = this.category,
        icon = this.icon,
        title = this.title,
        message = this.message,
        timeStamp = this.timeStamp
    )
}