package com.rohitchauhan.spendmate.domain.repo

import androidx.paging.PagingData
import com.rohitchauhan.spendmate.domain.model.GroupTransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun getAllTransactions(): Flow<PagingData<TransactionModel>>
    suspend fun insertTransaction(transactionModel: TransactionModel)
    suspend fun deleteTransaction(transactionModel: TransactionModel)
    suspend fun getTransactionById(transactionId: Int): TransactionModel?
    suspend fun getGroupTransactionById(transactionId: Int): GroupTransactionModel?
    suspend fun deleteAllTransactions()
    fun getTotalIncome(timeRange: Pair<Long,Long>): Flow<Long>
    fun getTotalExpense(timeRange: Pair<Long,Long>): Flow<Long>
    fun getTotalGroupExpense(timeRange: Pair<Long,Long>): Flow<Long>
    fun getTotalGroupIncome(timeRange:Pair<Long, Long>): Flow<Long>
    suspend fun saveGroupTransaction(groupTransaction: GroupTransactionModel)
    fun getAllGroupTransactions(): Flow<PagingData<GroupTransactionModel>>
    suspend fun deleteGroupTransaction(transactionId:Int)
}