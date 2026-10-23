package com.rohitchauhan.spendmate.data.local

import androidx.compose.ui.tooling.data.Group
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    @Query("select * from transactions order by timeStamp desc")
    fun getAllTransactions(): PagingSource<Int, TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Query("select * from transactions where id=:transactionId")
    suspend fun getTransactionById(transactionId: Int): TransactionEntity?

@Query("select * from group_transactions where id=:transactionId")
    suspend fun getGroupTransactionById(transactionId: Int): GroupTransactionEntity?

    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("delete from transactions")
    suspend fun deleteAllTransactions()

    @Query("select coalesce(sum(amount), 0) from transactions where type='Income' and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalIncomeSince(startTime: Long,endTime: Long): Flow<Long>

    @Query("select coalesce(sum(amount), 0) from transactions where type='Expense' and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalExpenseSince(startTime: Long,endTime: Long): Flow<Long>

    @Query("select coalesce(sum(amount), 0) from group_transactions where type='Income' and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalGroupIncomeSince(startTime:Long,endTime:Long): Flow<Long>

    @Query("select coalesce(sum(amount), 0) from group_transactions where type='Expense' and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalGroupExpenseSince(startTime:Long,endTime:Long): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGroupTransaction(groupTransaction: GroupTransactionEntity)

    @Query("select * from group_transactions")
    fun getAllGroupTransactions(): PagingSource<Int, GroupTransactionEntity>

    @Query("delete from group_transactions where id =:transactionId")
    suspend fun deleteGroupTransaction(transactionId: Int)
}