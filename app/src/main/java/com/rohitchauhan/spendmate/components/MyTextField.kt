package com.rohitchauhan.spendmate.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohitchauhan.spendmate.ui.theme.alata_reguler


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String ="",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onLeadingIconClick: () -> Unit = {},
    shape: RoundedCornerShape =  RoundedCornerShape(12.dp),
    containerColor: Color = Color.White,
    singleLine: Boolean = true,
    maxLine: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    fontFamily: FontFamily = alata_reguler,
    errorMessage: String="",

    ) {
    Column {
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = modifier.heightIn(min = 48.dp),
            textStyle = TextStyle(fontSize = 14.sp, fontFamily = fontFamily),
            singleLine = singleLine,
            maxLines = maxLine,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = remember { MutableInteractionSource() },
                    placeholder = {
                        Text(
                            text = placeHolder,
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                            color=Color.Gray
                        )
                    },
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    isError = isError,
                    shape = shape,
                    container = {
                        TextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = isError,
                            interactionSource = remember { MutableInteractionSource() },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = shape
                        )
                    }
                )
            }
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
