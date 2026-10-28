package com.rohitchauhan.spendmate.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.domain.usercases.CreateAccountUserCase
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
class SignUpScreenVM @Inject constructor(
    private val repo: Repo,
    private val createAccountUserCase: CreateAccountUserCase
): ViewModel() {
    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()
    fun onEmailChange(email:String){
        _signUpUiState.update{it.copy(email=email)}
    }
    fun onPasswordChange(password:String){
        _signUpUiState.update{it.copy(password=password)}
    }
    fun onConfirmPasswordChange(confirmPassword:String){
        _signUpUiState.update{it.copy(confirmPassword = confirmPassword)}
    }

    fun createAccount(){
        viewModelScope.launch {
            createAccountUserCase(
                email = _signUpUiState.value.email,
                password = _signUpUiState.value.password,
                confirmPassword = _signUpUiState.value.confirmPassword
            ).collect { result ->
                when(result) {
                    is ResultState.Error -> _uiEvent.emit(SignUpUiEvent.ShowToast(result.message))
                    ResultState.Idle -> {}
                    ResultState.Loading -> _uiEvent.emit(SignUpUiEvent.Loading)
                    is ResultState.Success -> _uiEvent.emit(SignUpUiEvent.NavigateBack)
                }
            }
        }
    }
}
sealed class SignUpUiEvent {
    data class ShowToast(val message: String) : SignUpUiEvent()
    object Loading: SignUpUiEvent()
    object NavigateBack: SignUpUiEvent()
}
data class SignUpUiState(
    val email:String = "",
    val password:String ="",
    val confirmPassword:String=""
)