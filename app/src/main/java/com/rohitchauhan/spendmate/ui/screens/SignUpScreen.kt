package com.rohitchauhan.spendmate.ui.screens

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.components.MyTextField
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.ui.viewModels.SignUpScreenVM
import com.rohitchauhan.spendmate.ui.viewModels.SignUpUiEvent

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    goBack:()-> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val viewModel = hiltViewModel<SignUpScreenVM>()

    val signUpUiState = viewModel.signUpUiState.collectAsStateWithLifecycle().value

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                SignUpUiEvent.Loading -> {
                    isLoading=true
                }
                SignUpUiEvent.NavigateBack -> {
                    goBack()
                }
                is SignUpUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(R.drawable.wallet_logo), null)
        Text(
            "SpendMate",
            fontSize = 18.sp,
            fontFamily = alata_reguler,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Create Account",
            fontSize = 18.sp,
            fontFamily = alata_reguler,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Start managing your money today",
            fontSize = 13.sp,
            fontFamily = alata_reguler,
            color = Color.Gray
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Email address", fontFamily = alata_reguler)
            MyTextField(
                value = signUpUiState.email,
                onValueChange = {viewModel.onEmailChange(it)},
                placeHolder = "you@gmail.com",
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                leadingIcon = {
                    Icon(painterResource(R.drawable.email), null)
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Password", fontFamily = alata_reguler)
            MyTextField(
                value = signUpUiState.password,
                onValueChange = { viewModel.onPasswordChange(it)},
                placeHolder = "At least 8 characters",
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                leadingIcon = {
                    Icon(painterResource(R.drawable.lock), null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        Icon(
                            painterResource(if (isPasswordVisible) R.drawable.eye else R.drawable.eye_off),
                            null
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Confirm password", fontFamily = alata_reguler)
            MyTextField(
                value = signUpUiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                placeHolder = "Re-enter password",
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                leadingIcon = {
                    Icon(painterResource(R.drawable.lock), null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        }
                    ) {
                        Icon(
                            painterResource(if (isConfirmPasswordVisible) R.drawable.eye else R.drawable.eye_off),
                            null
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
        }
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = {
                viewModel.createAccount()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = appColor
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if(isLoading){
                CircularProgressIndicator()
            }else {
                Text("Create Account", fontFamily = alata_reguler)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "By creating an account,you agree to our",
            fontFamily = alata_reguler,
            fontSize = 13.sp,
            color = Color.Gray
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Terms of Service", fontFamily = alata_reguler, color = appColor, fontSize = 13.sp)
            Text(" and ", fontSize = 13.sp)
            Text("Privacy Policy", fontFamily = alata_reguler, color = appColor, fontSize = 13.sp)
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 56.dp)
        ) {
            Text("Already have and account? ", fontFamily = alata_reguler, color = Color.Gray)
            Text(
                "Log In",
                fontFamily = alata_reguler,
                color = appColor,
                modifier = Modifier.clickable {
                    onLoginClick()
                })
        }
    }
}