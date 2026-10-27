package com.rohitchauhan.spendmate.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    //to show in ui
    @Query("select * from transactions order by timeStamp desc")
    fun getAllTransactionsPageByPage(): PagingSource<Int, TransactionEntity>
    //to upload to cloud (firebase)
    @Query("select * from transactions")
    suspend fun getAllTransactionList(): List<TransactionEntity>
    //for insert a transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    //to insert all the transaction coming from the cloud(firebase)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransactions(transactionList:List<TransactionEntity>)
    //to get transaction by its id
    @Query("select * from transactions where id=:transactionId")
    suspend fun getTransactionById(transactionId: Int): TransactionEntity?
    //to delete a particular transaction
    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)
    //to delete bulk transaction from the database
    @Query("delete from transactions")
    suspend fun deleteAllTransactions()
    //to get total income
    @Query("select coalesce(sum(amount), 0) from transactions where type='Income' and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalIncomeSince(startTime: Long, endTime: Long): Flow<Long>
    //to get total expense
    @Query("select coalesce(sum(amount), 0) from transactions where type='Expense' and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalExpenseSince(startTime: Long, endTime: Long): Flow<Long>

    @Query("select coalesce(sum(amount), 0) from transactions where type='Income' and isGroupTransaction=1 and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalGroupIncomeSince(startTime: Long, endTime: Long): Flow<Long>

    @Query("select coalesce(sum(amount), 0) from transactions where type='Expense' and isGroupTransaction=1 and timeStamp>=:startTime and timeStamp<:endTime")
    fun getTotalGroupExpenseSince(startTime: Long, endTime: Long): Flow<Long>
}