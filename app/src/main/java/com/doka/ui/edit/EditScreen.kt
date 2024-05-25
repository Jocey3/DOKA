package com.doka.ui.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.MainViewModel
import com.doka.R
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RectangleBorderColor
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeLight
import com.doka.ui.theme.RudeMid
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun EditScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var zoom by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var angle by remember { mutableStateOf(0f) }


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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 330.dp, height = 220.dp)
                    .dashedBorder(RectangleBorderColor, RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 179.dp, height = 127.dp)
                        .border(2.dp, RectangleBorderColor, RoundedCornerShape(8.dp))

                ) {
                    viewModel.currentBitmap?.let {

                        Image(
                            it.asImageBitmap(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .graphicsLayer {
                                    translationX = -offset.x * zoom
                                    translationY = -offset.y * zoom
                                    scaleX = zoom
                                    scaleY = zoom
                                    rotationZ = angle
                                    TransformOrigin(0f, 0f).also { transformOrigin = it }
                                },
                            contentDescription = "Image for edit",
                            contentScale = ContentScale.Crop

                        )
                    } ?: run {

                        Image(
                            painterResource(id = R.drawable.ic_launcher_foreground),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentDescription = "Image for edit",
                            contentScale = ContentScale.Crop

                        )
                    }

                }
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(
                    color = RudeMid,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(16.dp)

        ) {

            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(width = 234.dp, height = 139.dp)
                    .background(
                        color = RudeLight,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures(
                            onGesture = { gestureCentroid, gesturePan, gestureZoom, gestureRotate ->
                                val oldScale = zoom
                                val newScale = (zoom * gestureZoom).coerceIn(0.5f..5f)

                                // For natural zooming and rotating, the centroid of the gesture should
                                // be the fixed point where zooming and rotating occurs.
                                // We compute where the centroid was (in the pre-transformed coordinate
                                // space), and then compute where it will be after this delta.
                                // We then compute what the new offset should be to keep the centroid
                                // visually stationary for rotating and zooming, and also apply the pan.
                                offset =
                                    (offset + gestureCentroid / oldScale).rotateBy(gestureRotate) -
                                            (gestureCentroid / newScale + gesturePan / oldScale)
                                angle += gestureRotate
                                zoom = newScale
                            }
                        )
                    }
            ) {

            }

        }
    }

}

fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadians = angle * PI / 180
    return Offset(
        (x * cos(angleInRadians) - y * sin(angleInRadians)).toFloat(),
        (x * sin(angleInRadians) + y * cos(angleInRadians)).toFloat()
    )
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 4.dp,
    dashWidth: Dp = 8.dp,
    gapWidth: Dp = 13.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, this)

    val path = Path()
    path.addOutline(outline)

    val stroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
            phase = 0f
        )
    )

    this.drawContent()

    drawPath(
        path = path,
        style = stroke,
        color = color
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditScreenPreview() {
    DOKATheme {
        EditScreen()
    }
}
