package com.rohitchauhan.spendmate.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routs{
    @Serializable
    data object DashBoardRout: Routs()
    @Serializable
    data class AddTransactionRout(
        val transactionId:Int=0,
        val isPersonTransaction: Boolean=false
    ): Routs()
    @Serializable
    data class AddGroupTransactionScreen(
        val transactionId:Int=0
    ):Routs()
    @Serializable
    data object SettingRout:Routs()
    @Serializable
    data object SignUpScreen:Routs()
    @Serializable
    data object SignInScreen:Routs()
}