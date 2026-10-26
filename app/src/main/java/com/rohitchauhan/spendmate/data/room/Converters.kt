package com.rohitchauhan.spendmate.data.room

import androidx.room.TypeConverter
import com.rohitchauhan.spendmate.domain.model.TransactionType

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }
    @TypeConverter
    fun toTransactionType(value:String): TransactionType{
        return TransactionType.valueOf(value)
    }
}