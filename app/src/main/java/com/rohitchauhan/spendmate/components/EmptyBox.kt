package com.rohitchauhan.spendmate.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.ui.theme.alata_reguler

@Composable
fun EmptyBox() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(R.drawable.empty),contentDescription="empty", modifier = Modifier.size(280.dp))
        Text("No Transactions yet!", fontSize = 28.sp, fontFamily = alata_reguler)
        Spacer(Modifier.height(8.dp))
        Text("Your transaction will appear hear.\nones you add it will show here", textAlign = TextAlign.Center, fontFamily = alata_reguler)
    }
}