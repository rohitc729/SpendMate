package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.TransactionUiModel
import com.rohitchauhan.spendmate.utils.formatDateHeader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonScreenVM @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    fun onSearchQueryChanged(value: String) {
        _searchQuery.value = value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionToPerson = _searchQuery
        .flatMapLatest { query ->
            repo.getAllTransactions()
                .map { pagingData ->
                    pagingData.filter { transactionModel ->
                        val hasPersonName = transactionModel.personName.isNotBlank()
                        val mathchesQuery = query.isBlank() || transactionModel.personName.contains(query, ignoreCase = true) ||
                                transactionModel.title.contains(query, ignoreCase = true)
                        hasPersonName&&mathchesQuery
                    }
                }
                .cachedIn(viewModelScope)
        }.map { pagingData ->
            pagingData.map {transaction->
                TransactionUiModel.TransactionItem(transaction) as TransactionUiModel
            }.insertSeparators { before : TransactionUiModel?, after : TransactionUiModel?->
                val beforeItem = (before as? TransactionUiModel.TransactionItem)?.transaction
                val afterItem = (after as? TransactionUiModel.TransactionItem)?.transaction

                val beforeData = beforeItem?.timeStamp?.let { formatDateHeader(it) }
                val afterData = afterItem?.timeStamp?.let { formatDateHeader(it) }

                if(before==null){
                    afterData?.let { TransactionUiModel.Header(it) }
                }else if(afterData!=beforeData && afterData!=null){
                      TransactionUiModel.Header(afterData)
                }else{
                    null
                }
            }
        }
    fun deleteTransaction(transaction: TransactionModel){
        viewModelScope.launch {
            repo.deleteTransaction(
                transaction
            )
        }
    }
}