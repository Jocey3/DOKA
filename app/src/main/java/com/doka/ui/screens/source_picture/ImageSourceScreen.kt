package com.doka.ui.screens.source_picture

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.MainViewModel
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid
import com.doka.ui.theme.TextCancelColor
import com.doka.ui.theme.TextSimpleColor
import com.doka.ui.util.ButtonDefault
import java.io.IOException


@Composable
fun ImageSourceScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: ImageSourceViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isLoading by remember { mutableStateOf(true) }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        try {
            val inputStream = uri?.let { context.contentResolver?.openInputStream(it) }
            if (inputStream != null) {
                try {
                    val inputStream = uri.let { context.contentResolver?.openInputStream(it) }
                    if (inputStream != null) {
                        sharedVM.currentBitmap = BitmapFactory.decodeStream(inputStream)
                        navigateNext.invoke()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


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
            ButtonDefault(text = "Select Photo") {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            ButtonDefault(text = "Stop Spinner") {
                isLoading = !isLoading
            }
        }
        BottomPanel(Modifier.align(Alignment.BottomCenter), isLoading, navigateBack = navigateBack)
    }

}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    navigateBack: () -> Unit = {},
) {
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
        if (isLoading) {
            CustomCircularProgressIndicator(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )
            Text(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 31.dp),
                fontSize = 16.sp,
                text = "Waiting for the image from the server",
                color = TextSimpleColor
            )
            Text(
                modifier = Modifier
                    .clickable { navigateBack() }
                    .padding(bottom = 16.dp),
                fontSize = 20.sp,
                text = "Cancel",
                fontWeight = FontWeight.Bold,
                color = TextCancelColor
            )
        } else {
            Text(
                text = "Bottom Panel",
                color = TextSimpleColor
            )
        }
    }
}

@Composable
fun CustomCircularProgressIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Restart
        ), label = ""
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6D2C2C), Color(0xFF391616)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Box(modifier = modifier.size(40.dp)) {
        Canvas(modifier = Modifier.size(40.dp)) {
            val strokeWidth = 4.dp.toPx()
            val radius = size.minDimension / 2 - strokeWidth / 2
            val center = Offset(size.width / 2, size.height / 2)

            drawArc(
                brush = gradient,
                startAngle = animatedAngle,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokeWidth)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        ImageSourceScreen()
    }
}
