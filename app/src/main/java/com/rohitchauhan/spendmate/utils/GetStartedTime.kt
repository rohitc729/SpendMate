package com.rohitchauhan.spendmate.utils

import java.util.Calendar

fun getTimeRange(filter: String): Pair<Long, Long> {
    val startCal = Calendar.getInstance()
    val endCal = Calendar.getInstance()
    when (filter.lowercase()) {
        "this month" -> {
            startCal.set(Calendar.DAY_OF_MONTH, 1)
            startCal.set(Calendar.HOUR_OF_DAY, 0)
            startCal.set(Calendar.MINUTE, 0)
            startCal.set(Calendar.SECOND, 0)
            startCal.set(Calendar.MILLISECOND, 0)

            endCal.timeInMillis = startCal.timeInMillis
            endCal.add(Calendar.MONTH, 1)
        }
        "last month" -> {
            startCal.add(Calendar.MONTH, -1)
            startCal.set(Calendar.DAY_OF_MONTH, 1)
            startCal.set(Calendar.HOUR_OF_DAY, 0)
            startCal.set(Calendar.MINUTE, 0)
            startCal.set(Calendar.SECOND, 0)
            startCal.set(Calendar.MILLISECOND, 0)

            endCal.timeInMillis = startCal.timeInMillis
            endCal.add(Calendar.MONTH, 1)
        }
        "this year" -> {
            startCal.set(Calendar.DAY_OF_YEAR, 1)
            startCal.set(Calendar.HOUR_OF_DAY, 0)
            startCal.set(Calendar.MINUTE, 0)
            startCal.set(Calendar.SECOND, 0)
            startCal.set(Calendar.MILLISECOND, 0)

            endCal.timeInMillis = startCal.timeInMillis
            endCal.add(Calendar.YEAR, 1)
        }
        "last year" -> {
            startCal.add(Calendar.YEAR, -1)
            startCal.set(Calendar.DAY_OF_YEAR, 1)
            startCal.set(Calendar.HOUR_OF_DAY, 0)
            startCal.set(Calendar.MINUTE, 0)
            startCal.set(Calendar.SECOND, 0)
            startCal.set(Calendar.MILLISECOND, 0)

            endCal.timeInMillis = startCal.timeInMillis
            endCal.add(Calendar.YEAR, 1)
        }
    }
    return Pair(startCal.timeInMillis, endCal.timeInMillis)
}