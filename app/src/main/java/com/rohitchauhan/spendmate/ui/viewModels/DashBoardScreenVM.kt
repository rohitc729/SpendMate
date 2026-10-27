package com.rohitchauhan.spendmate.ui.viewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.ExpenseCategoryItem
import com.rohitchauhan.spendmate.utils.TransactionUiModel
import com.rohitchauhan.spendmate.utils.formatDateHeader
import com.rohitchauhan.spendmate.utils.getTimeRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardScreenVM @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilterCategory = MutableStateFlow(ExpenseCategoryItem())
    val selectedFilterCategory = _selectedFilterCategory.asStateFlow()

    private val _selectedMonthForSummary = MutableStateFlow("this month")
    val selectedMonthForSummary = _selectedMonthForSummary.asStateFlow()
    fun onMonthChangedForSummary(month: String){
        _selectedMonthForSummary.value=month
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalIncome : StateFlow<Long> = _selectedMonthForSummary
        .flatMapLatest { month->
            val timeRange = getTimeRange(month)
            repo.getTotalIncome(timeRange)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )
    @OptIn(ExperimentalCoroutinesApi::class)
    val totalExpense : StateFlow<Long> = _selectedMonthForSummary
        .flatMapLatest { month->
            val timeRange = getTimeRange(month)
            repo.getTotalExpense(timeRange)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )
    val totalAmount = combine(totalExpense,totalIncome){expense,income->
        income-expense
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0L
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedTransactions = combine(_searchQuery, _selectedFilterCategory) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, filterCategory) ->
        repo.getAllTransactionsPageByPage()
            .map { pagingData ->
                pagingData.filter { transaction ->
                    val matchesQuery =
                        !transaction.isGroupTransaction&&
                        (
                        query.isBlank() ||
                        transaction.title.contains(query, ignoreCase = true) ||
                        transaction.personName?.contains(query, ignoreCase = true) ==true||
                        transaction.category.contains(query, ignoreCase = true)
                        )
                    val matchesCategory = filterCategory.name.isBlank() ||
                        filterCategory.name.equals("All Transactions", ignoreCase = true) ||
                        filterCategory.name.equals("All", ignoreCase = true) ||
                        transaction.category.equals(filterCategory.name, ignoreCase = true)

                    matchesQuery && matchesCategory
                }
            }
    }.map { pagingData ->
        pagingData.map {     transaction ->
            TransactionUiModel.TransactionItem(transaction) as TransactionUiModel
        }.insertSeparators { before: TransactionUiModel?, after: TransactionUiModel? ->
            val beforeItem = (before as? TransactionUiModel.TransactionItem)?.transaction
            val afterItem = (after as? TransactionUiModel.TransactionItem)?.transaction

            val beforeDate = beforeItem?.timeStamp?.let { formatDateHeader(it) }
            val afterDate = afterItem?.timeStamp?.let { formatDateHeader(it) }

            if (before == null) {
                afterDate?.let { TransactionUiModel.Header(it) }
            } else if (afterDate != beforeDate && afterDate != null) {
                TransactionUiModel.Header(afterDate)
            } else {
                null
            }
        }
    }
    .cachedIn(viewModelScope)

    fun insertTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            repo.insertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            repo.deleteTransaction(transaction)
        }
    }

    fun onSearchQueryChanged(value: String) {
        _searchQuery.value = value
    }

    fun onFilterValueChanged(it: ExpenseCategoryItem) {
        _selectedFilterCategory.value = it
    }

}
