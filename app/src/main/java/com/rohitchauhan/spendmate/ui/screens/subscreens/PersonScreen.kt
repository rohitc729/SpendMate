package com.rohitchauhan.spendmate.ui.screens.subscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.rohitchauhan.spendmate.components.EmptyBox
import com.rohitchauhan.spendmate.components.TransactionCard
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.viewModels.PersonScreenVM
import com.rohitchauhan.spendmate.utils.TransactionUiModel

@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    viewModel: PersonScreenVM
) {
    val transactions = viewModel.transactionToPerson.collectAsLazyPagingItems()

    val loadState =transactions.loadState
    when (loadState.refresh) {
        is LoadState.Loading if transactions.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error if transactions.itemCount == 0 -> {
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

        is LoadState.NotLoading if transactions.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                EmptyBox()
            }
        }

        else -> {
            LazyColumn(
                modifier=Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                items(
                    count = transactions.itemCount,
                    key = {index->
                        when (val uiModel = transactions[index]){
                            is TransactionUiModel.Header -> {"Header_${uiModel.dateString}"}
                            is TransactionUiModel.TransactionItem -> {"Tx_${uiModel.transaction.id}"}
                            null -> {"Placeholder_$index"}
                        }
                    }
                ){index->
                    when(val uiModel = transactions[index]){
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
                                        Routs.AddTransactionRout(uiModel.transaction.id, isPersonTransaction = true)
                                    )
                                }
                            )
                        }
                        null -> {
                            Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                EmptyBox()
                            }
                        }
                    }
                }
            }
        }
    }

}