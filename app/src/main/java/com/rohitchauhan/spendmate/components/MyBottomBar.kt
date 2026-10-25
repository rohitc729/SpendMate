package com.rohitchauhan.spendmate.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.getColumnIndexOrThrow
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.utils.BottomTab

@Composable
fun MyBottomBar(
    modifier: Modifier = Modifier,
    tabs: List<BottomTab>,
    currentTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(bottom = 32.dp, start = 32.dp, end = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .shadow(12.dp, shape = RoundedCornerShape(24.dp))
                .height(80.dp)
                .background(
                    color = appColor,
                    shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
                )
//                .border(
//                    BorderStroke(color = Color.White.copy(alpha = 0.5f), width = 1.5.dp),
//                    shape = RoundedCornerShape(24.dp)
//                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                Row(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable(
                            onClick = {
                                onTabSelected(index)
                            }
                        )
                        .animateContentSize()
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = "person icon",
                        tint = if (currentTabIndex == index) MaterialTheme.colorScheme.primary else Color.Black
                    )
                    AnimatedVisibility(
                        visible = currentTabIndex == index,
                        enter = fadeIn(animationSpec = tween(durationMillis = 100))+ expandHorizontally(animationSpec = tween(100)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 100)) + shrinkHorizontally(animationSpec = tween(100))
                    ) {
                        Text(
                            tab.title,
                            fontSize = 12.sp,
                            color = if (currentTabIndex == index) MaterialTheme.colorScheme.primary else Color.Black,
                            modifier = Modifier.padding(start = 8.dp),
                            fontFamily = alata_reguler
                        )
                    }
                }
            }
        }
    }
}