package com.rohitchauhan.spendmate.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.room.util.TableInfo
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.ui.viewModels.SettingScreenVM
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rohitchauhan.spendmate.components.NotLoggedInMessage
import com.rohitchauhan.spendmate.components.NotUploadedMessage
import com.rohitchauhan.spendmate.utils.ResultState
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.logging.SimpleFormatter

@Composable
fun SettingScreen(
    goBack: () -> Unit,
    rootNavController: NavHostController
) {
    val viewModel: SettingScreenVM = hiltViewModel()
    val isLoggedIn = viewModel.isLoggedIn()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = goBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrow back"
                    )
                }
                Text(
                    "Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = alata_reguler
                )
            }
            if (isLoggedIn) {
                NotUploadedMessage(viewModel)
            } else {
                NotLoggedInMessage(
                    viewModel = viewModel,
                    onCrateAccountClick = {
                        rootNavController.navigate(Routs.SignUpScreen)
                    },
                    onSignInClick = {
                        rootNavController.navigate(Routs.SignInScreen)
                    }
                )
            }
        }
    }
}


