package com.rohitchauhan.spendmate.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rohitchauhan.spendmate.components.MyTextField
import com.rohitchauhan.spendmate.domain.model.GroupTransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionType
import com.rohitchauhan.spendmate.ui.viewModels.GroupTransactionScreenVM
import com.rohitchauhan.spendmate.utils.defaultCategories
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupTransactionScreen(
    onBackClick: () -> Unit,
    transactionId: Int = 0
) {
    val transactionType by remember { mutableStateOf(TransactionType.Expense) }
    val selectedDateInMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var amountText by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var totalNoOfPersonText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(defaultCategories.first()) }
    var isSaving by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val viewModel : GroupTransactionScreenVM= hiltViewModel()


    var initialGroupTransaction by remember { mutableStateOf<GroupTransactionModel?>(null) }
    LaunchedEffect(transactionId) {
        if(transactionId!=0){
           val gt =  viewModel.getGroupTransactionById(transactionId)
            if(gt!=null){
                initialGroupTransaction = gt
                amountText=gt.amount.toString()
                titleText=gt.title
                messageText=gt.message
                totalNoOfPersonText=gt.personCount.toString()
                selectedCategory=defaultCategories.firstOrNull { it.name==gt.category } ?: defaultCategories.first()
            }
        }
    }

    val isEditing = transactionId != 0
    val hasChanged = remember(
        amountText,
        titleText,
        messageText,
        totalNoOfPersonText,
        selectedCategory,
        initialGroupTransaction
    ) {
        if (!isEditing || initialGroupTransaction == null) {
            true
        } else {
            val currentAmount = amountText.toDoubleOrNull() ?: 0.0
            val currentPersonCount = totalNoOfPersonText.toIntOrNull() ?: 1
            currentAmount != initialGroupTransaction?.amount ||
                    titleText != initialGroupTransaction?.title ||
                    messageText != initialGroupTransaction?.message ||
                    currentPersonCount != initialGroupTransaction?.personCount ||
                    selectedCategory.name != initialGroupTransaction?.category
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Group_Expense") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(24.dp))
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.5.dp, Color.White), RoundedCornerShape(24.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(text = "Amound", fontSize = 13.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "₹",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (transactionType == TransactionType.Expense) Color(0xffff5252) else Color(
                            0xff4cac50
                        )
                    )
                    MyTextField(
                        value = amountText,
                        onValueChange = {
                            amountText=it
                        },
                        placeHolder = "0.0",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                defaultCategories.forEach { categoryItem ->
                    val isSelected = selectedCategory.name == categoryItem.name
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) categoryItem.color else Color.White,
                        label = "categoryColor"
                    )
                    Row(
                        modifier =
                            Modifier
                                .shadow(
                                    if (isSelected) 6.dp else 2.dp,
                                    RoundedCornerShape(20.dp)
                                )
                                .background(bgColor, RoundedCornerShape(20.dp))
                                .border(
                                    BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary else Color.White
                                    ),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedCategory = categoryItem
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(categoryItem.emoji, fontSize = 16.sp)
                        Text(
                            categoryItem.name,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = Color(0xff2d3142)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            MyTextField(
                value = titleText,
                onValueChange = {
                    titleText= it
                },
                placeHolder = "Note / Title (e.g. Swiggy Lunch)",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
            )
            Spacer(Modifier.height(16.dp))
            MyTextField(
                value = messageText,
                onValueChange = {
                    messageText= it
                },
                placeHolder = "Message / Description (Optional)",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
            )
            Spacer(Modifier.height(16.dp))
            MyTextField(
                value = totalNoOfPersonText,
                onValueChange = {
                    totalNoOfPersonText=it
                },
                placeHolder = "Total no of Persons(1 or 2 ..)",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp)),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Date:", fontSize = 13.sp, color = Color.Gray)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = dateFormatter.format(selectedDateInMillis),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xff2d3142)
                    )
                }
                Text(
                    "Change",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(20.dp
            ))
            Button(
                enabled = !isSaving,
                onClick = {
                    if (isSaving) return@Button
                    val amount = amountText.toDoubleOrNull()?:0.0
                    if(amount>0 && totalNoOfPersonText.isNotEmpty()){
                        isSaving = true
                        val newGroupTransaction= GroupTransactionModel(
                            id=transactionId,
                            type = TransactionType.Expense,
                            amount =amount,
                            perPersonAmount = amount/totalNoOfPersonText.toInt(),
                            personCount = totalNoOfPersonText.toInt(),
                            category =selectedCategory.name ,
                            icon = selectedCategory.emoji,
                            title = titleText,
                            message = messageText,
                            timeStamp = System.currentTimeMillis()
                        )
                        viewModel.saveGroupTransaction(newGroupTransaction)
                        onBackClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(54.dp),
                shape=RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffff5252)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text("Save transaction", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}