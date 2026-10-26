package com.rohitchauhan.spendmate.ui.screens.subscreens

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.InteroperableComposeUiNode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rohitchauhan.spendmate.components.EmptyBox
import com.rohitchauhan.spendmate.components.TransactionCard
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.viewModels.DashBoardScreenVM
import com.rohitchauhan.spendmate.utils.ExpenseCategoryItem
import com.rohitchauhan.spendmate.utils.TransactionUiModel
import com.rohitchauhan.spendmate.utils.defaultCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreen(viewModel: DashBoardScreenVM, rootNavController: NavHostController) {
    val items = viewModel.pagedTransactions.collectAsLazyPagingItems()
    var isFilterMenuVisible by remember { mutableStateOf(false) }
    var summaryBottomShitVisible by remember { mutableStateOf(false) }
    var showMonthDropDownMenu by remember { mutableStateOf(false) }

    val selectedMonth by viewModel.selectedMonthForSummary.collectAsStateWithLifecycle()
    val totalIncome = viewModel.totalIncome.collectAsStateWithLifecycle().value
    val totalExpense = viewModel.totalExpense.collectAsStateWithLifecycle().value
    val totalAmount = totalIncome - totalExpense

    if (summaryBottomShitVisible)
        ModalBottomSheet(
            onDismissRequest = { summaryBottomShitVisible = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Row {
                        Text(selectedMonth, modifier = Modifier.clickable {
                            showMonthDropDownMenu = true
                        }, fontFamily = alata_reguler)
                        Spacer(Modifier.width(4.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowLeft,"", modifier = Modifier.rotate(-90f))
                    }
                    DropdownMenu(
                        expanded = showMonthDropDownMenu,
                        onDismissRequest = { showMonthDropDownMenu = false },
                    ) {
                        listOf("this month", "last month", "this year", "last year").forEach {
                            DropdownMenuItem(
                                text = { Text(it, fontFamily = alata_reguler) },
                                onClick = {
                                    showMonthDropDownMenu = false
                                    viewModel.onMonthChangedForSummary(it)
                                }
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color = Color(0xff4caf50))
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Total income:\n$totalIncome", textAlign = TextAlign.Center)
                        }

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color = Color(0xffff5252))
                                .weight(1f), contentAlignment = Alignment.Center
                        ) {
                            Text("total expenses:\n$totalExpense", textAlign = TextAlign.Center)
                        }
                    }
                    Text("total amount: $totalAmount")
                }
            }
        }
    var selectedCategory by remember { mutableStateOf("All transactions") }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                Row (
                    modifier = Modifier.clickable {
                        isFilterMenuVisible = true
                    }
                ){
                    Text(
                        text = selectedCategory,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = alata_reguler
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowLeft,
                        contentDescription = "arrow",
                        modifier = Modifier.rotate(-90f)
                    )
                }
                DropdownMenu(
                    expanded = isFilterMenuVisible,
                    onDismissRequest = { isFilterMenuVisible = false },
                    shape = RoundedCornerShape(12.dp),
                    containerColor = Color.White
                ) {
                    defaultCategories.plus(
                        ExpenseCategoryItem(
                            name = "All transactions",
                            emoji = "",
                            color = Color.Transparent,
                        )
                    ).forEach {
                        DropdownMenuItem(
                            text = { Text(it.name, fontFamily = alata_reguler) },
                            onClick = {
                                viewModel.onFilterValueChanged(it)
                                selectedCategory = it.name
                                isFilterMenuVisible = false
                            }
                        )
                    }
                }
            }
            Row (verticalAlignment = Alignment.CenterVertically){
                Text("\uD83D\uDCDD")
                Text(
                    text = "Summary",
                    modifier = Modifier.clickable {
                        summaryBottomShitVisible = true
                    },
                    fontFamily = alata_reguler,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
        val loadState = items.loadState
        when (loadState.refresh) {
            is LoadState.Loading if items.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error if items.itemCount == 0 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Failed to load transactions", fontFamily = alata_reguler)

                    Button(onClick = { items.retry() }) {
                        Text("Retry", fontFamily = alata_reguler)
                    }
                }
            }

            is LoadState.NotLoading if items.itemCount == 0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyBox()
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )
                {
                    items(
                        count = items.itemCount,
                        key = { index ->
                            when (val uiModel = items[index]) {
                                is TransactionUiModel.Header -> "header_${uiModel.dateString}"
                                is TransactionUiModel.TransactionItem -> "tx_${uiModel.transaction.id}"
                                null -> "placeholder_$index"
                            }
                        }
                    ) {  index ->
                        when (val uiModel = items[index]) {
                            is TransactionUiModel.Header -> {
                                Text(
                                    text = uiModel.dateString,
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontFamily = alata_reguler
                                )
                            }

                            is TransactionUiModel.TransactionItem -> {
                                TransactionCard(
                                    transaction = uiModel.transaction,
                                    onDeleteMenuClick = {
                                        viewModel.deleteTransaction(uiModel.transaction)
                                    },
                                    onItemClick = {
                                        rootNavController.navigate(
                                            Routs.AddTransactionRout(
                                                uiModel.transaction.id
                                            )
                                        )
                                    }
                                )
                            }

                            null -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EmptyBox()
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}