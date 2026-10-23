package com.rohitchauhan.spendmate.ui.screens.subscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.rohitchauhan.spendmate.components.TransactionCard2
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.viewModels.GroupTransactionScreenVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTransactionsScreen(viewModel: GroupTransactionScreenVM,rootNavController: NavHostController) {

    val transactions = viewModel.groupTransactions.collectAsLazyPagingItems()
    var showBottomSheet by remember { mutableStateOf(false) }
    val totalIncome = viewModel.totalIncome.collectAsStateWithLifecycle().value
    val totalExpense = viewModel.totalExpense.collectAsStateWithLifecycle().value
    val totalAmount = totalIncome - totalExpense
    var showMonthDropDownMenu by remember { mutableStateOf(false) }
    val selectedMonth by viewModel.selectedMonthForSummary.collectAsStateWithLifecycle()
    if(showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {showBottomSheet=false}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Text("$selectedMonth ▼", modifier = Modifier.clickable {
                        showMonthDropDownMenu = true
                    })
                    DropdownMenu(
                        expanded = showMonthDropDownMenu,
                        onDismissRequest = { showMonthDropDownMenu = false },
                    ) {
                        listOf("this month", "last month", "this year", "last year").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
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
    }
    Column (modifier = Modifier.fillMaxSize()){
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.End) {
            Text(
                text = "Summary\uD83D\uDCDD",
                modifier = Modifier.clickable {
                    showBottomSheet = true
                }
            )
        }
        val loadState = transactions.loadState
        when (loadState.refresh)
        {
            is LoadState.Loading if transactions.itemCount==0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error if transactions.itemCount==0 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Failed to load transactions")

                    Button(onClick = { transactions.retry() }) {
                        Text("Retry")
                    }
                }
            }

            is LoadState.NotLoading if transactions.itemCount==0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("No Transactions", fontSize = 20.sp)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 4.dp)
                )
                {
                    items(
                        count = transactions.itemCount,
                        key = transactions.itemKey(),
                        contentType = transactions.itemContentType()
                    ) {
                        val transaction = transactions[it]
                        if (transaction != null) {
                            TransactionCard2(
                                transaction,
                                onItemClick = {
                                    rootNavController.navigate(
                                        Routs.AddGroupTransactionScreen(
                                            transaction.id
                                        )
                                    )
                                },
                                onDeleteMenuClick = {
                                    viewModel.deleteTransaction(transaction.id)
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}