package com.rohitchauhan.spendmate.domain.usercases

import com.rohitchauhan.spendmate.domain.repo.Repo
import javax.inject.Inject

class SignInUserCase @Inject constructor(
    private val repo: Repo
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repo.logiIn(email,password)
}