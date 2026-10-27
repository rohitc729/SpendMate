package com.rohitchauhan.spendmate.domain.repo

import androidx.paging.PagingData
import com.rohitchauhan.spendmate.data.firebase.TransactionDTO
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun getAllTransactionList(): List<TransactionModel>
    fun getAllTransactionsPageByPage(): Flow<PagingData<TransactionModel>>

    suspend fun insertTransaction(transactionModel: TransactionModel)
    suspend fun insertAllTransactions(transactionList: List<TransactionModel>)

    suspend fun deleteTransaction(transactionModel: TransactionModel)
    suspend fun deleteAllTransactions()

    suspend fun getTransactionById(transactionId: Int): TransactionModel?
    fun getTotalIncome(timeRange: Pair<Long, Long>): Flow<Long>
    fun getTotalExpense(timeRange: Pair<Long, Long>): Flow<Long>

    fun getTotalGroupExpense(timeRange: Pair<Long, Long>): Flow<Long>
    fun getTotalGroupIncome(timeRange: Pair<Long, Long>): Flow<Long>

    //Authentication
    fun createAccount(email: String, password: String): Flow<ResultState<Boolean>>
    fun logiIn(email: String, password: String): Flow<ResultState<Boolean>>

    //Firebase backup / sync
    suspend fun uploadTransactions(transactions: List<TransactionModel>): Flow<ResultState<Boolean>>
    suspend fun downloadTransactions(): Flow<ResultState<List<TransactionModel>>>
}