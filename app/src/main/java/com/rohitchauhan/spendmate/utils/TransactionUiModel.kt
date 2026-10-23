package com.rohitchauhan.spendmate.utils

import com.rohitchauhan.spendmate.domain.model.TransactionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

sealed interface TransactionUiModel {
    data class Header(val dateString: String) : TransactionUiModel
    data class TransactionItem(val transaction: TransactionModel) : TransactionUiModel
}

fun formatDateHeader(timestamp: Long): String {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply { timeInMillis = timestamp }

    return when {
        now.get(Calendar.ERA) == target.get(Calendar.ERA) &&
        now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
        now.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR) -> "Today"

        else -> {
            val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
            if (yesterday.get(Calendar.ERA) == target.get(Calendar.ERA) &&
                yesterday.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)) {
                "Yesterday"
            } else {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(timestamp)
            }
        }
    }
}
