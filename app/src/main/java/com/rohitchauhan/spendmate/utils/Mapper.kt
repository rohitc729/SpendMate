package com.rohitchauhan.spendmate.utils

import com.rohitchauhan.spendmate.data.firebase.TransactionDTO
import com.rohitchauhan.spendmate.data.room.GroupTransactionEntity
import com.rohitchauhan.spendmate.data.room.TransactionEntity
import com.rohitchauhan.spendmate.domain.model.GroupTransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionType


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
fun List<TransactionModel>.toTransactionEntityList(): List<TransactionEntity>{
    return this.map {
        it.toTransactionEntity()
    }
}
fun List<TransactionEntity>.toTransactionModelList():List<TransactionModel>{
    return this.map {
        it.toTransactionModel()
    }
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
fun TransactionModel.toTransactionDTO(): TransactionDTO{
    return TransactionDTO(
        id = this.id,
        type = this.type.name,
        amount = this.amount,
        category = this.category,
        personName = this.personName,
        title = this.title,
        message = this.message,
        timeStamp = this.timeStamp,
        icon = this.icon
    )
}
fun TransactionDTO.toTransactionModel(): TransactionModel{
    return TransactionModel(
        id = this.id?:0,
        type = TransactionType.entries.firstOrNull{
            it.name.equals(this.type, ignoreCase = true)
        }?:TransactionType.Expense,
        amount = this.amount?:0.0,
        category = this.category?:"",
        personName = this.personName?:"",
        title = this.title?:"",
        message = this.message?:"",
        timeStamp = this.timeStamp?:0L,
        icon = this.icon?:"",
    )
}
fun List<TransactionModel>.toTransactionDTOList(): List<TransactionDTO>{
    return this.map {
        it.toTransactionDTO()
    }
}

@JvmName("dtoToTransactionModelList")
fun List<TransactionDTO>.toTransactionModelList(): List<TransactionModel>{
    return this.map {
        it.toTransactionModel()
    }
}
