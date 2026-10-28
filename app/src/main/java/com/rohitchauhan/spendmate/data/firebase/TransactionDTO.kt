package com.rohitchauhan.spendmate.data.firebase

import com.google.firebase.database.PropertyName
import com.rohitchauhan.spendmate.domain.model.TransactionType

data class TransactionDTO(
    val id: Int? = null,
    val type: String? = null,
    val amount: Double? = null,
    val category: String? = null,
    val personName: String? = null,
    val title: String? = null,
    val icon: String? = null,
    val message: String? = null,
    val timeStamp: Long? = null,
    val perPersonAmount: Double? = null,
    val personCount: Int? = null,
    @get:PropertyName("isGroupTransaction")
    @field:PropertyName("isGroupTransaction")
    val isGroupTransaction: Boolean = false
)