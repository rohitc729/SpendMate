package com.rohitchauhan.spendmate.utils

import androidx.compose.ui.graphics.Color


data class ExpenseCategoryItem(
    val name: String,
    val emoji: String,
    val color: Color
){
    constructor():this("","",Color.Transparent)
}
val defaultCategories = listOf(
    ExpenseCategoryItem("Food", "🍕", Color(0xFFFFE0B2)),
    ExpenseCategoryItem("Travel", "🚗", Color(0xFFBBDEFB)),
    ExpenseCategoryItem("Shopping", "🛍️", Color(0xFFF8BBD0)),
    ExpenseCategoryItem("Bills", "💡", Color(0xFFFFF9C4)),
    ExpenseCategoryItem("Entertainment", "🍿", Color(0xFFE1BEE7)),
    ExpenseCategoryItem("Health", "🏥", Color(0xFFC8E6C9)),
    ExpenseCategoryItem("Salary", "💵", Color(0xFFD1C4E9)),
    ExpenseCategoryItem("Vegetable", "\uD83E\uDD66", Color(0xFFCFD8DC)),
    ExpenseCategoryItem("Glossary", "\uD83D\uDED2", Color(0xFFCFD8DC)),
    ExpenseCategoryItem("Electronics", "📱", Color(0xFFCFD8DC)),
    ExpenseCategoryItem("Alcoholic Drinks", "🍻", Color(0xFFCFD8DC)),
    ExpenseCategoryItem("Other", "❓", Color(0xFFCFD8DC)),
)