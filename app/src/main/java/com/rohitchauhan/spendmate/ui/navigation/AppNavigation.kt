package com.rohitchauhan.spendmate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rohitchauhan.spendmate.ui.screens.AddGroupTransactionScreen
import com.rohitchauhan.spendmate.ui.screens.AddTransactionScreen
import com.rohitchauhan.spendmate.ui.screens.DashBoardScreen
import com.rohitchauhan.spendmate.ui.screens.ProfileScreen
import com.rohitchauhan.spendmate.ui.screens.SettingScreen
import com.rohitchauhan.spendmate.ui.screens.SignInScreen
import com.rohitchauhan.spendmate.ui.screens.SignUpScreen

@Composable
fun AppNavigation() {
    val rootNavController = rememberNavController()
    NavHost(navController = rootNavController, startDestination = Routs.DashBoardRout){
        composable < Routs.DashBoardRout>{
            DashBoardScreen(
                onSettingClick = {
                    rootNavController.navigate(Routs.SettingRout)
                },
                onAddTransaction = {forPersonTransaction:Boolean->
                    rootNavController.navigate(Routs.AddTransactionRout(transactionId = 0, isPersonTransaction = forPersonTransaction))
                },
                onProfileClick = {
                    rootNavController.navigate(Routs.ProfileScreen)
                },
                rootNavController = rootNavController
            )
        }
        composable < Routs.AddTransactionRout>{
            val data = it.toRoute<Routs.AddTransactionRout>()
            AddTransactionScreen (
                isPersonTransaction = data.isPersonTransaction,
                transactionId = data.transactionId,
                onBackPress = {rootNavController.popBackStack()}
            )
        }
        composable < Routs.AddGroupTransactionScreen>{
            val data = it.toRoute<Routs.AddGroupTransactionScreen>()
            AddGroupTransactionScreen(
                transactionId = data.transactionId,
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }
        composable < Routs.SettingRout>{
            SettingScreen(
                onUploadClick = {},
                onDownloadClick = {},
                rootNavController=rootNavController
            )
        }
        composable < Routs.SignUpScreen>{
            SignUpScreen(
                onLoginClick = {
                    rootNavController.navigate(Routs.SignInScreen)
                },
                goBack = {
                    rootNavController.popBackStack()
                }
            )
        }
        composable < Routs.SignInScreen>{
            SignInScreen(
                onSignUpClick = {
                    rootNavController.navigate(Routs.SignUpScreen)
                },
                gotoBack = {
                    rootNavController.popBackStack()
                }
            )
        }
        composable < Routs.ProfileScreen>{
            ProfileScreen(

            )
        }
    }
}