package com.rohitchauhan.spendmate.data.repoimpl

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.rohitchauhan.spendmate.data.firebase.TransactionDTO
import com.rohitchauhan.spendmate.data.pref.SharedPrefService
import com.rohitchauhan.spendmate.data.room.TransactionDAO
import com.rohitchauhan.spendmate.domain.model.GroupTransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.ResultState
import com.rohitchauhan.spendmate.utils.toGroupTransactionEntity
import com.rohitchauhan.spendmate.utils.toGroupTransactionModel
import com.rohitchauhan.spendmate.utils.toTransactionDTOList
import com.rohitchauhan.spendmate.utils.toTransactionEntity
import com.rohitchauhan.spendmate.utils.toTransactionEntityList
import com.rohitchauhan.spendmate.utils.toTransactionModel
import com.rohitchauhan.spendmate.utils.toTransactionModelList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val transactionDAO: TransactionDAO,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPrefService: SharedPrefService
) : Repo{
    override suspend fun getAllTransactionList(): List<TransactionModel> {
        return transactionDAO.getAllTransactionList().toTransactionModelList()
    }

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
        sharedPrefService.setAnyThingToUpload(true)
    }

    override suspend fun insertAllTransactions(transactionList: List<TransactionModel>) {
        transactionDAO.insertAllTransactions(transactionList.toTransactionEntityList())
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

    //Authentication
    override fun createAccount(
        email: String,
        password: String
    ): Flow<ResultState<Boolean>> =flow {
        emit(ResultState.Loading)
        try {
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            emit(ResultState.Success(true))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage?:"something went wrong"))
        }
    }

    override fun logiIn(
        email: String,
        password: String
    ): Flow<ResultState<Boolean>> = flow{
        emit(ResultState.Loading)
        try{
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            emit(ResultState.Success(true))
        }
        catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage?:"something went wrong"))
        }
    }

    override suspend fun uploadTransactions(transactions: List<TransactionModel>) : Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        val userId = firebaseAuth.currentUser?.uid
        if(userId.isNullOrBlank()){
            emit(ResultState.Error("user not authenticated"))
            return@flow
        }
        try {
            val transactionDto = transactions.toTransactionDTOList()
            val transactionRef = firebaseDatabase.reference.child("transactions").child(userId)
            transactionRef.setValue(transactionDto).await()
            sharedPrefService.saveLastSyncTimeStamp()
            sharedPrefService.setAnyThingToUpload(false)
            emit(ResultState.Success(true))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage?:"something went wrong"))
        }
    }
    override suspend fun downloadTransactions():Flow<ResultState<List<TransactionModel>>> = flow {

        emit(ResultState.Loading)
        val userId = firebaseAuth.currentUser?.uid

        if(userId.isNullOrBlank()){
            emit(ResultState.Error("user not authenticated"))
            return@flow
        }
        try {
            val transactionRef = firebaseDatabase.reference.child("transactions").child(userId)
            val snapshot = transactionRef.get().await()
            val transactions = snapshot.children.mapNotNull {
                it.getValue(TransactionDTO::class.java)?.toTransactionModel()
            }
            sharedPrefService.setAnyThingToUpload(false)
            sharedPrefService.saveLastSyncTimeStamp()
           emit(ResultState.Success(transactions))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage?:"Failed to download transactions"))
        }
    }
}