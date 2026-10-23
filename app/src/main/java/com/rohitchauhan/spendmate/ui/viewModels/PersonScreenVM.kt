package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.room.Transaction
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
        }
    fun deleteTransaction(transaction: TransactionModel){
        viewModelScope.launch {
            repo.deleteTransaction(
                transaction
            )
        }
    }
}