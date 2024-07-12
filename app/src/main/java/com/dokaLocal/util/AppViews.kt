package com.dokaLocal.util

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokaLocal.ui.theme.ButtonBackgroundColor
import com.dokaLocal.ui.theme.ButtonTextColor
import com.dokaLocal.ui.theme.RedClick

@Composable
fun ButtonDefault(modifier: Modifier = Modifier, text: String, listener: () -> Unit) {
    var selected by remember { mutableStateOf(false) }
    val color = remember { Animatable(ButtonBackgroundColor) }
    LaunchedEffect(selected) {
        color.animateTo(if (selected) RedClick else ButtonBackgroundColor)
    }
    Box(
        modifier = modifier
            .height(70.dp)
            .background(color.value, shape = RoundedCornerShape(40.dp))
            .clickable {
                selected = !selected
                listener.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = text,
            fontSize = 24.sp,
            color = ButtonTextColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}