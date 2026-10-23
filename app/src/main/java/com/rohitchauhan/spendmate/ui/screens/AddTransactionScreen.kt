package com.rohitchauhan.spendmate.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rohitchauhan.spendmate.components.MyTextField
import com.rohitchauhan.spendmate.domain.model.TransactionModel
import com.rohitchauhan.spendmate.domain.model.TransactionType
import com.rohitchauhan.spendmate.ui.viewModels.AddTransactionScreenVM
import com.rohitchauhan.spendmate.utils.defaultCategories
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    transactionId: Int = 0, onBackPress: () -> Unit,
    isPersonTransaction: Boolean
) {
    val isPersonTransaction by remember { mutableStateOf(isPersonTransaction) }

    val viewModel: AddTransactionScreenVM = hiltViewModel()

    val context = LocalContext.current
    var amountText by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.Expense) }
    var selectedCategory by remember { mutableStateOf(defaultCategories.first().name) }
    var selectedCategoryEmoji by remember { mutableStateOf(defaultCategories.first().emoji) }
    var noteText = remember { mutableStateOf("") }
    var personNameText by remember { mutableStateOf("") }
    var selectedDateInMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var isSaving by remember { mutableStateOf(false) }
    val calender = remember(selectedDateInMillis) {
        Calendar.getInstance().apply { timeInMillis = selectedDateInMillis }
    }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    var personTextFieldErrorMessage by remember { mutableStateOf("") }
    var initialTransaction by remember { mutableStateOf<TransactionModel?>(null) }
    LaunchedEffect(transactionId) {
        if (transactionId != 0) {
            val tx = viewModel.getTransactionById(transactionId)
            if (tx != null) {
                initialTransaction = tx
                amountText = tx.amount.toString()
                transactionType = tx.type
                selectedCategory = tx.category
                selectedCategoryEmoji = tx.icon
                noteText.value = tx.title ?: ""
                personNameText = tx.personName ?: ""
                selectedDateInMillis = tx.timeStamp
            }
        }
    }

    val isEditing = transactionId != 0
    val hasChanged = remember(
        amountText,
        transactionType,
        selectedCategory,
        noteText.value,
        personNameText,
        selectedDateInMillis,
        initialTransaction
    ) {
        if (!isEditing || initialTransaction == null) {
            true
        } else {
            val currentAmount = amountText.toDoubleOrNull() ?: 0.0
            currentAmount != initialTransaction?.amount ||
                    transactionType != initialTransaction?.type ||
                    selectedCategory != initialTransaction?.category ||
                    noteText.value != (initialTransaction?.title ?: "") ||
                    personNameText != (initialTransaction?.personName ?: "") ||
                    selectedDateInMillis != initialTransaction?.timeStamp
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Expense", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPress
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .background(color = Color.White, RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.5.dp, Color.White), RoundedCornerShape(24.dp))
                    .padding(6.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val isExpense = transactionType == TransactionType.Expense
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = if (isExpense) Color(0xffff5252) else Color.Transparent)
                        .clickable { transactionType = TransactionType.Expense }
                        .padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Expense",
                        color = if (isExpense) Color.White else Color(0xff757575),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = if (!isExpense) Color(0xFF4CAF50) else Color.Transparent)
                        .clickable { transactionType = TransactionType.Income }
                        .padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Income",
                        color = if (!isExpense) Color.White else Color(0xff757575),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp))
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.5.dp, Color.White), RoundedCornerShape(24.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column {
                    Text("Amount", fontSize = 12.sp, color = Color.Gray)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            onValueChange = { input ->
                                if (input.isNotEmpty() || input.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                                    amountText = input
                                }
                            },
                            placeHolder = "0.00",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = Color.Transparent
                        )
                    }
                }
            }
            Spacer(Modifier.height(if(isPersonTransaction) 0.dp else 20.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                if(!isPersonTransaction) {
                    Text(
                        "Category",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xff2d3142),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    )
                    {
                        defaultCategories.forEach { categoryItem ->
                            val isSelected = selectedCategory == categoryItem.name
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
                                            selectedCategory = categoryItem.name
                                            selectedCategoryEmoji = categoryItem.emoji
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
                }
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xff2d3142)
                    )
                    MyTextField(
                        value = noteText.value,
                        onValueChange = { noteText.value = it },
                        placeHolder = "Note / Title (e.g. Swiggy Lunch)${if(isPersonTransaction) " -Optional" else "" }",
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        containerColor = Color.White
                    )
                    MyTextField(
                        value = personNameText,
                        onValueChange = {
                            personNameText = it
                            personTextFieldErrorMessage=""
                        },
                        placeHolder = "Person / Merchant ${if(isPersonTransaction) "" else "(Optional)" }",
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        containerColor = Color.White,
//                        isError = personTextFieldErrorMessage.isNotEmpty(),
                        errorMessage=personTextFieldErrorMessage
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                        .background(Color.White, RoundedCornerShape(18.dp))
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val cal = Calendar.getInstance()
                                    cal.set(year, month, dayOfMonth)
                                    selectedDateInMillis = cal.timeInMillis
                                },
                                calender.get(Calendar.YEAR),
                                calender.get(Calendar.MONTH),
                                calender.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                Spacer(Modifier.height(24.dp))
                Button(
                    enabled = !isSaving && hasChanged,
                    onClick ={
                        if (isSaving) return@Button
                        if(isPersonTransaction && personNameText.isBlank()){
                            personTextFieldErrorMessage = "Person name is required"
                            return@Button
                        }else{
                            personTextFieldErrorMessage=""
                        }
                        val pareAmount=amountText.toDoubleOrNull()?:0.0
                        if(pareAmount>0){
                            isSaving = true
                            val newTransaction= TransactionModel(
                                id = transactionId,
                                type = transactionType,
                                amount = pareAmount,
                                category = if(isPersonTransaction) "Other" else selectedCategory,
                                personName = personNameText,
                                title = noteText.value.ifBlank { selectedCategory },
                                message = noteText.value.ifBlank { selectedCategory },
                                timeStamp = selectedDateInMillis,
                                icon = if(isPersonTransaction) "\uD83E\uDDD1\uD83C\uDFFB" else selectedCategoryEmoji
                            )
                            viewModel.insertTransaction(newTransaction)
                            onBackPress()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(10.dp, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (transactionType == TransactionType.Expense) Color(
                            0xffff5252
                        ) else Color(0xff4caf50)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (transactionId == 0) "Save Transaction" else "Update Transaction",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}