package com.rohitchauhan.spendmate.data.repoimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rohitchauhan.spendmate.data.local.TransactionDAO
import com.rohitchauhan.spendmate.domain.model.GroupTransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.toGroupTransactionEntity
import com.rohitchauhan.spendmate.utils.toGroupTransactionModel
import com.rohitchauhan.spendmate.utils.toTransactionEntity
import com.rohitchauhan.spendmate.utils.toTransactionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val transactionDAO: TransactionDAO
) : Repo{
    override fun getAllTransactions(): Flow<PagingData<TransactionModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDAO.getAllTransactions()
            }
        ).flow.map {pagingData ->
            pagingData.map {
                it.toTransactionModel()
            }
        }
    }

    override suspend fun insertTransaction(transactionModel: TransactionModel) {
        transactionDAO.insertTransaction(transactionModel.toTransactionEntity())
    }

    override suspend fun deleteTransaction(transactionModel: TransactionModel) {
      transactionDAO.deleteTransaction(transactionModel.toTransactionEntity())
    }

    override suspend fun getTransactionById(transactionId: Int): TransactionModel? {
        return transactionDAO.getTransactionById(transactionId)?.toTransactionModel()
    }

    override suspend fun getGroupTransactionById(transactionId: Int): GroupTransactionModel? {
        return transactionDAO.getGroupTransactionById(transactionId)?.toGroupTransactionModel()
    }

    override suspend fun deleteAllTransactions() {
        transactionDAO.deleteAllTransactions()
    }

    override fun getTotalIncome(timeRange: Pair<Long,Long>): Flow<Long> {
        return  transactionDAO.getTotalIncomeSince(timeRange.first,timeRange.second)
    }

    override fun getTotalExpense(timeRange: Pair<Long,Long>): Flow<Long> {
        return transactionDAO.getTotalExpenseSince(timeRange.first,timeRange.second)
    }
    override fun getTotalGroupIncome(timeRange: Pair<Long,Long>): Flow<Long> {
        return  transactionDAO.getTotalGroupIncomeSince(timeRange.first,timeRange.second)
    }

    override fun getTotalGroupExpense(timeRange:Pair<Long, Long>): Flow<Long> {
        return transactionDAO.getTotalGroupExpenseSince(timeRange.first,timeRange.second)
    }

    override suspend fun saveGroupTransaction(groupTransaction: GroupTransactionModel) {
        transactionDAO.saveGroupTransaction(groupTransaction.toGroupTransactionEntity())
    }

    override fun getAllGroupTransactions(): Flow<PagingData<GroupTransactionModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDAO.getAllGroupTransactions()
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toGroupTransactionModel()
            }
        }
    }

    override suspend fun deleteGroupTransaction(transactionId: Int) {
       transactionDAO.deleteGroupTransaction(transactionId)
    }


}