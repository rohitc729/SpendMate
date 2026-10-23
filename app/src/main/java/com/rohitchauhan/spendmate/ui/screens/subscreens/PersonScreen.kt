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
import com.rohitchauhan.spendmate.components.TransactionCard
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.viewModels.PersonScreenVM

@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    viewModel: PersonScreenVM
) {
    val transactions = viewModel.transactionToPerson.collectAsLazyPagingItems()

    val loadState =transactions.loadState
    when{
        loadState.refresh is LoadState.Loading && transactions.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        loadState.refresh is LoadState.Error && transactions.itemCount == 0 -> {
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
        loadState.refresh is LoadState.NotLoading&& transactions.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text("No Transactions", fontSize = 20.sp)
            }
        }
        else->{
            LazyColumn(
                modifier=Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                items(
                    count = transactions.itemCount,
                    key = transactions.itemKey(),
                    contentType = transactions.itemContentType()
                ){
                    val transaction = transactions[it]
                    if(transaction!=null){
                        TransactionCard(
                            transaction = transaction,
                            onDeleteMenuClick = {
                                viewModel.deleteTransaction(transaction)
                            },
                            onItemClick = {
                                rootNavController.navigate(
                                    Routs.AddTransactionRout(transaction.id, isPersonTransaction = true)
                                )
                            }
                        )
                    }else{
                        Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            Text(text = "No Transactions", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }

}