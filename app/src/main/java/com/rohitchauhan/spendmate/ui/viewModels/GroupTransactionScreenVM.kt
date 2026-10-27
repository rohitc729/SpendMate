package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.getTimeRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupTransactionScreenVM @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    fun onSearchQueryChanged(value: String) {
        _searchQuery.value = value
    }

    private val _selectedMonthForSummary = MutableStateFlow("this month")
    val selectedMonthForSummary = _selectedMonthForSummary.asStateFlow()
    fun onMonthChangedForSummary(month: String) {
        _selectedMonthForSummary.value = month
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalIncome: StateFlow<Long> = _selectedMonthForSummary
        .flatMapLatest { month ->
            val timeRange = getTimeRange(month)
            repo.getTotalGroupIncome(timeRange)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalExpense: StateFlow<Long> = _selectedMonthForSummary
        .flatMapLatest {
            val timeRange = getTimeRange(it)
            repo.getTotalGroupExpense(timeRange)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val groupTransactions = _searchQuery.flatMapLatest { query ->
        repo.getAllTransactionsPageByPage()
            .map { pagingData ->
                pagingData.filter { item ->
                    item.isGroupTransaction &&
                    (
                       query.isBlank() ||
                       item.title.contains(query, ignoreCase = true) ||
                       item.category.contains(query, ignoreCase = true) ||
                       item.message.contains(query, ignoreCase = true)
                   )
                }
            }
    }.cachedIn(viewModelScope)

    fun saveGroupTransaction(groupTransaction: TransactionModel) {
        viewModelScope.launch {
            repo.insertTransaction(groupTransaction)
        }
    }

    fun deleteTransaction(transactionId: TransactionModel) {
        viewModelScope.launch {
            repo.deleteTransaction(transactionId)
        }
    }

    suspend fun getGroupTransactionById(transactionId: Int): TransactionModel? =
        repo.getTransactionById(transactionId)
}