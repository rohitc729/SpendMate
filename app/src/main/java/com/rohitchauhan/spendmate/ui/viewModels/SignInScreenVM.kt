package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ActionCodeEmailInfo
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.domain.usercases.SignInUserCase
import com.rohitchauhan.spendmate.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenVM @Inject constructor(
    private val repo: Repo,
    private val signInUserCase: SignInUserCase
): ViewModel() {
    private val _singInUiState = MutableStateFlow(SignInUiState())
    val singInUiState = _singInUiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SignInUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    fun onEmailChange(value: String){
        _singInUiState.update {
            it.copy(email = value)
        }
    }
    fun onPasswordChange(value: String){
        _singInUiState.update {
            it.copy(password = value)
        }
    }
    fun logIn(){
        viewModelScope.launch {
            signInUserCase(
                _singInUiState.value.email,
                _singInUiState.value.password
            ).collect { result->
                when(result){
                    is ResultState.Error ->_uiEvent.emit(SignInUiEvent.ShowToast(result.message))
                    ResultState.Idle -> {}
                    ResultState.Loading -> _uiEvent.emit(SignInUiEvent.Loading)
                    is ResultState.Success -> _uiEvent.emit(SignInUiEvent.NavigateBack)
                }
            }
        }
    }
}
data class SignInUiState(
    val email:String="",
    val password:String=""
)
sealed class SignInUiEvent{
    data class ShowToast(val message: String) : SignInUiEvent()
    object Loading: SignInUiEvent()
    object NavigateBack: SignInUiEvent()
}