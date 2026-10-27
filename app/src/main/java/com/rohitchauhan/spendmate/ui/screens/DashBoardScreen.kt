package com.rohitchauhan.spendmate.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.components.MyBottomBar
import com.rohitchauhan.spendmate.components.MyTextField
import com.rohitchauhan.spendmate.ui.navigation.Routs
import com.rohitchauhan.spendmate.ui.screens.subscreens.AllTransactionsScreen
import com.rohitchauhan.spendmate.ui.screens.subscreens.GroupTransactionsScreen
import com.rohitchauhan.spendmate.ui.screens.subscreens.PersonScreen
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.ui.viewModels.DashBoardScreenVM
import com.rohitchauhan.spendmate.ui.viewModels.GroupTransactionScreenVM
import com.rohitchauhan.spendmate.ui.viewModels.PersonScreenVM
import com.rohitchauhan.spendmate.utils.BottomTab
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    onSettingClick: () -> Unit,
    onAddTransaction: (Boolean) -> Unit,
    rootNavController: NavHostController
) {
    var isMoreMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val dashBoardViewModel: DashBoardScreenVM = hiltViewModel()
    var searchText by remember { mutableStateOf("") }
    var searchMode by remember { mutableStateOf(false) }

    val pages = listOf<BottomTab>(
        BottomTab(title = "All Transactions", icon = R.drawable.home_unselected),
        BottomTab(title = "Group Transaction", icon = R.drawable.group),
        BottomTab(title = "Person", icon = R.drawable.user)
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )
    val scope = rememberCoroutineScope()

    val personScreenVM: PersonScreenVM = hiltViewModel()
    val groupTransactionVM: GroupTransactionScreenVM = hiltViewModel()
    val totalAmount = dashBoardViewModel.totalAmount.collectAsStateWithLifecycle().value

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            searchMode = false
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MyBottomBar(
                tabs = pages,
                currentTabIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page=index,
                        )
                    }
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    if (searchMode) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
                        ) {
                            MyTextField(
                                value = searchText,
                                onValueChange = {
                                    searchText = it
                                    when (pagerState.currentPage) {
                                        0 -> {
                                            dashBoardViewModel.onSearchQueryChanged(it)
                                        }

                                        1 -> {
                                            groupTransactionVM.onSearchQueryChanged(it)
                                        }

                                        2 -> {
                                            personScreenVM.onSearchQueryChanged(it)
                                        }
                                    }
                                },
                                placeHolder = "Searches for transaction",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                containerColor = Color.White,
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                }
                            )
                        }
                    } else {
                        Row {
                            Text(
                                "Total Amount: ",
                                fontFamily = alata_reguler,
                            )
                            Text(
                                totalAmount.toString(),
                                fontFamily = alata_reguler,
                                color =  if(totalAmount>=0) Color(0xff4caf50) else Color(0xffff5252)
                            )
                        }
                    }
                },
                actions = {
                    if (!searchMode) {
                        IconButton(
                            onClick = {
                                searchMode = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon"
                            )
                        }
                        Box {
                            IconButton(
                                onClick = {
                                    isMoreMenuExpanded = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More icon"
                                )
                            }
                            DropdownMenu(
                                expanded = isMoreMenuExpanded,
                                onDismissRequest = { isMoreMenuExpanded = false },
                                containerColor = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        isMoreMenuExpanded = false
                                        onSettingClick()
                                    }
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = {
                                searchMode = false
                                searchText = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "cancel search"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    when (pagerState.currentPage) {
                        0, 2 -> {
                            onAddTransaction(
                                pagerState.currentPage == 2
                            )
                        }

                        1 -> {
                            rootNavController.navigate(Routs.AddGroupTransactionScreen(0))
                        }
                    }

                },
                text = {
                    Text("Add", fontFamily = alata_reguler, color = Color.White)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add transaction icon",
                        tint = Color.White
                    )
                },
                shape = CircleShape,
                containerColor = appColor
            )

        },
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> AllTransactionsScreen(dashBoardViewModel, rootNavController)
                1 -> GroupTransactionsScreen(
                    viewModel = groupTransactionVM,
                    rootNavController = rootNavController
                )

                2 -> PersonScreen(rootNavController = rootNavController, viewModel = personScreenVM)
            }
        }
    }
}