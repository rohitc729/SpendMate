package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rohitchauhan.spendmate.data.pref.SharedPrefService
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.ResultState
import com.rohitchauhan.spendmate.utils.ResultState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingScreenVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repo: Repo,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {
    private val _uploadState = MutableSharedFlow<ResultState<Boolean>>()
    val uploadState = _uploadState.asSharedFlow()
    private val _downloadState = MutableSharedFlow<ResultState<List<TransactionModel>>>()
    val downloadState = _downloadState.asSharedFlow()

    private val _loginState = MutableSharedFlow<ResultState<Boolean>>()
    val loginState = _loginState.asSharedFlow()
    private val _createAccountState = MutableSharedFlow<ResultState<Boolean>>()
    val createAccountState = _createAccountState.asSharedFlow()

    private val _lastSyncTime = MutableStateFlow(sharedPrefService.getLastSyncTimeStamp())
    val lastSyncTime: StateFlow<Long> = _lastSyncTime.asStateFlow()

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            repo.createAccount(email, password).collect {
                when (it) {
                    is Error -> _createAccountState.emit(Error(it.message))
                    Idle -> _createAccountState.emit(Idle)
                    Loading -> _createAccountState.emit(Loading)
                    is Success -> _createAccountState.emit(Success(true))
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repo.logiIn(email, password).collect {
                when (it) {
                    is Error -> _loginState.emit(Error(it.message))
                    Idle -> _loginState.emit(Idle)
                    Loading -> _loginState.emit(Loading)
                    is Success -> _loginState.emit(Success(true))
                }
            }
        }
    }

    fun downloadTransactions() {
        viewModelScope.launch {
            repo.downloadTransactions().collect {
                when (it) {
                    is Error -> _downloadState.emit(Error(it.message))
                    Idle -> _downloadState.emit(Idle)
                    Loading -> _downloadState.emit(ResultState.Loading)
                    is Success -> {
                        _downloadState.emit(Success(it.data))
                        _lastSyncTime.value = sharedPrefService.getLastSyncTimeStamp()
                        if (it.data.isNotEmpty()) {
                            repo.deleteAllTransactions()
                            repo.insertAllTransactions(it.data)
                        }
                    }
                }
            }
        }
    }

    fun uploadTransactions() {
        viewModelScope.launch {
            val transactions = repo.getAllTransactionList()
            repo.uploadTransactions(transactions = transactions)
                .collect {
                    when (it) {
                        is ResultState.Error -> _uploadState.emit(ResultState.Error(it.message))
                        ResultState.Idle -> _uploadState.emit(ResultState.Idle)
                        ResultState.Loading -> _uploadState.emit(ResultState.Loading)
                        is ResultState.Success -> {
                            _lastSyncTime.value = sharedPrefService.getLastSyncTimeStamp()
                            _uploadState.emit(ResultState.Success(true))
                        }
                    }
                }
        }
    }

}