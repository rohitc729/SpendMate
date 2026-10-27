package com.rohitchauhan.spendmate.ui.viewModels

import androidx.lifecycle.ViewModel
import com.rohitchauhan.spendmate.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInScreenVM @Inject constructor(
    private val repo: Repo
): ViewModel() {
}