package com.rohitchauhan.spendmate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionType
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.utils.formatDateHeader

@Composable
fun TransactionCard(
    transaction: TransactionModel,
    onDeleteMenuClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val formattedDate = remember(transaction.timeStamp) {
        formatDateHeader(transaction.timeStamp)
    }
    var isMenuExpended by remember { mutableStateOf(false) }
    var isIncome by remember { mutableStateOf(transaction.type == TransactionType.Income) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onItemClick() }
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(color = Color(0xffff5252).copy(alpha = .15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(transaction.icon, fontSize = 24.sp)
        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(
                transaction.personName.ifEmpty { transaction.title.ifEmpty { transaction.category } },
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = alata_reguler
            )
            Row(verticalAlignment = Alignment.CenterVertically){
            Text(transaction.category, fontSize = 12.sp, color = Color.Gray, fontFamily = alata_reguler)
                VerticalDivider(thickness = 1.dp, modifier = Modifier.height(16.dp).padding(horizontal = 4.dp))
                Text(formattedDate, fontSize = 12.sp, color = Color.Gray, fontFamily = alata_reguler)
            }
        }
        Text(
            text = if (isIncome) "+${transaction.amount}" else "-${transaction.amount}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isIncome) Color(0xff4caf50) else Color(0xffff5252),
            fontFamily = alata_reguler
        )
        Box {
            IconButton(
                onClick = {
                    isMenuExpended = true
                }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "menu icon")
            }
            DropdownMenu(
                expanded = isMenuExpended,
                onDismissRequest = { isMenuExpended = false },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color.White
            ) {
                DropdownMenuItem(
                    text = { Text("Delete", fontFamily = alata_reguler) },
                    onClick = {
                        onDeleteMenuClick()
                    }
                )
            }
        }
    }
}