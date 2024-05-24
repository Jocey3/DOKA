package com.doka.ui.util

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.doka.ui.theme.ButtonBackgroundColor
import com.doka.ui.theme.ButtonTextColor

@Composable
fun ButtonDefault(text: String, listener: () -> Unit) {
    Button(
        onClick = { listener.invoke() },
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonBackgroundColor
        )
    ) {
        Text(text, fontSize = 24.sp, color = ButtonTextColor)
    }
}