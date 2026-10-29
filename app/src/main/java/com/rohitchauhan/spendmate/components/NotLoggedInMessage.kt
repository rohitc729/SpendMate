package com.rohitchauhan.spendmate.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.ui.viewModels.SettingScreenVM

@Composable
fun NotLoggedInMessage(
    viewModel: SettingScreenVM,
    onCrateAccountClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.notloggedin),
            contentDescription = "cloud image",
            modifier = Modifier.size(250.dp)
        )
        Text(
            "You're not logged in",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontFamily = alata_reguler,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "It looks like you haven't created an account yet.\nLog in or sign up to continue",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
            fontSize = 13.sp,
            fontFamily = alata_reguler,
            color = Color.Gray
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                onCrateAccountClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = appColor
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(
                4.dp
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.add_user),
                    contentDescription = "add-user image",
                    tint = Color.White
                )
                Text("Create Account", fontSize = 12.sp)
            }
        }
        Text(
            "Already have an account? Log In",
            fontSize = 12.sp,
            fontFamily = alata_reguler,
            color = appColor,
            modifier = Modifier.clickable {
                onSignInClick()
            })
    }
}
