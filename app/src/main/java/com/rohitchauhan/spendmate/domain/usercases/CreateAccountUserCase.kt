package com.rohitchauhan.spendmate.domain.usercases

import com.rohitchauhan.spendmate.domain.repo.Repo
import com.rohitchauhan.spendmate.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CreateAccountUserCase @Inject constructor(
    private val repo: Repo
) {
    suspend operator fun invoke(email:String,password:String,confirmPassword: String): Flow<ResultState<Boolean>> {
        val errorMessage = when {
            email.isBlank() ->
                "Email can't be empty"

            password.isBlank() ->
                "Password can't be empty"

            password.length < 8 ->
                "Password should be at least 8 characters"

            password != confirmPassword ->
                "Password and confirm password don't match"

            else -> null
        }

        return if (errorMessage != null) {
            flowOf(ResultState.Error(errorMessage))
        } else {
            repo.createAccount(email, password)
        }
    }
}