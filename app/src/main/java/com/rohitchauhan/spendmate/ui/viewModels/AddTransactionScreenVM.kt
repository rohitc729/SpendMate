package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionScreenVM @Inject constructor(
    private val repo : Repo
) : ViewModel(){

    fun insertTransaction(transactionModel: TransactionModel){
        viewModelScope.launch {
            repo.insertTransaction(transactionModel)
        }
    }

    suspend fun getTransactionById(transactionId: Int): TransactionModel? {
        return repo.getTransactionById(transactionId)
    }
}