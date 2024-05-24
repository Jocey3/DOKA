package com.doka.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.MainViewModel
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid
import com.doka.ui.theme.TextSimpleColor


@Composable
fun EditScreen(viewModel: MainViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RudeDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


        }
        BottomPanel(Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun BottomPanel(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                color = RudeMid,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(16.dp)

    ) {

        Text(
            text = "Bottom Panel",
            color = TextSimpleColor
        )

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        EditScreen()
    }
}
