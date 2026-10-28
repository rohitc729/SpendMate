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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.rohitchauhan.spendmate.ui.viewModels.SignInScreenVM
import com.rohitchauhan.spendmate.ui.viewModels.SignInUiEvent

@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit,
    gotoBack: () -> Unit
) {
    val viewModel = hiltViewModel<SignInScreenVM>()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val signInState = viewModel.singInUiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                SignInUiEvent.Loading -> {
                    isLoading = true
                }

                SignInUiEvent.NavigateBack -> {
                    isLoading = false
                    gotoBack()
                }

                is SignInUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(30.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.wallet_logo), null)
        Text(
            "SpendMate",
            fontFamily = alata_reguler,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Welcome back!",
            fontSize = 18.sp,
            fontFamily = alata_reguler,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Log in to continue to SpendMate",
            fontFamily = alata_reguler,
            fontSize = 13.sp,
            color = Color.Gray
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Email address", fontFamily = alata_reguler)
            MyTextField(
                value = signInState.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
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
                value = signInState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
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
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = {
                viewModel.logIn()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColor
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Log In", fontFamily = alata_reguler)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Forget password?", fontFamily = alata_reguler, color = appColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier.width(100.dp)
            )
            Text(
                "Or continue with",
                fontFamily = alata_reguler,
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier.width(100.dp)
            )

        }
        OutlinedButton(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.google_g), null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(24.dp))
                Text("Continue with google", fontFamily = alata_reguler)
            }
        }
        Spacer(Modifier.height(24.dp))
        Row {
            Text("Don't have an account? ", fontFamily = alata_reguler, color = Color.Gray)
            Text(
                "Sign Up",
                fontFamily = alata_reguler,
                color = appColor,
                modifier = Modifier.clickable {
                    onSignUpClick()
                })
        }
    }
}