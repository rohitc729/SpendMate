package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rohitchauhan.spendmate.data.pref.SharedPrefService
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingScreenVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repo: Repo,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {
    private val _uploadState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val uploadState = _uploadState.asStateFlow()
    private val _downloadState =
        MutableStateFlow<ResultState<List<TransactionModel>>>(ResultState.Idle)
    val downloadState = _downloadState.asStateFlow()

    private val _loginState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val loginState = _loginState.asStateFlow()
    private val _createAccountState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val createAccountState = _createAccountState.asStateFlow()

    private val _lastSyncTime = MutableStateFlow(sharedPrefService.getLastSyncTimeStamp())
    val lastSyncTime: StateFlow<Long> = _lastSyncTime.asStateFlow()

    private val _hasPendingUploads = MutableStateFlow(sharedPrefService.isAnyThingToUpload())
    val hasPendingUploads: StateFlow<Boolean> = _hasPendingUploads.asStateFlow()

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            repo.createAccount(email, password).collect {
                _createAccountState.value = it
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repo.logiIn(email, password).collect {
                _loginState.value = it
            }
        }
    }

    fun downloadTransactions() {
        viewModelScope.launch {
            repo.downloadTransactions().collect {
                _downloadState.value = it
                if (it is ResultState.Success) {
                    _downloadState.value = ResultState.Success(it.data)
                    _lastSyncTime.value = sharedPrefService.getLastSyncTimeStamp()
                    _hasPendingUploads.value = sharedPrefService.isAnyThingToUpload()
                    if (it.data.isNotEmpty()) {
                        repo.deleteAllTransactions()
                        repo.insertAllTransactions(it.data)
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
                    _uploadState.value = it
                    if(it is ResultState.Success){
                        _lastSyncTime.value = sharedPrefService.getLastSyncTimeStamp()
                        _hasPendingUploads.value = sharedPrefService.isAnyThingToUpload()
                    }
                }
        }
    }
}