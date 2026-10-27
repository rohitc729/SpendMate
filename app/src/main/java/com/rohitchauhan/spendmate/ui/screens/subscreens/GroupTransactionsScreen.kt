package com.rohitchauhan.spendmate.ui.screens.subscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.rohitchauhan.spendmate.components.EmptyBox
import com.rohitchauhan.spendmate.components.TransactionCard
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.viewModels.GroupTransactionScreenVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTransactionsScreen(viewModel: GroupTransactionScreenVM,rootNavController: NavHostController) {

    val transactions = viewModel.groupTransactions.collectAsLazyPagingItems()
    val totalIncome = viewModel.totalIncome.collectAsStateWithLifecycle().value
    val totalExpense = viewModel.totalExpense.collectAsStateWithLifecycle().value
    val totalAmount = totalIncome - totalExpense
    var showMonthDropDownMenu by remember { mutableStateOf(false) }
    val selectedMonth by viewModel.selectedMonthForSummary.collectAsStateWithLifecycle()

    Column (modifier = Modifier.fillMaxSize()){
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
                    Text("Failed to load transactions", fontFamily = alata_reguler)

                    Button(onClick = { transactions.retry() }) {
                        Text("Retry", fontFamily = alata_reguler)
                    }
                }
            }

            is LoadState.NotLoading if transactions.itemCount==0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    EmptyBox()
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
                            TransactionCard(
                                transaction,
                                onItemClick = {
                                    rootNavController.navigate(
                                        Routs.AddGroupTransactionScreen(
                                            transaction.id
                                        )
                                    )
                                },
                                onDeleteMenuClick = {
                                    viewModel.deleteTransaction(transaction)
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}