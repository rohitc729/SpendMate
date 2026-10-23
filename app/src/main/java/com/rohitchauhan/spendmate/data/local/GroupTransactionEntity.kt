package com.rohitchauhan.spendmate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rohitchauhan.spendmate.domain.model.TransactionType

@Entity(tableName = "group_transactions")
data class GroupTransactionEntity(
    @PrimaryKey(autoGenerate = true)val id :Int=0,
    val type: TransactionType,
    val amount: Double,
    val perPersonAmount: Double,
    val personCount:Int,
    val category:String,
    val icon: String,
    val title:String,
    val message:String,
    val timeStamp:Long
)