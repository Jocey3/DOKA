package com.doka.ui.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.MainViewModel
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RectangleBorderColor
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeLight
import com.doka.ui.theme.RudeMid


@Composable
fun EditScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(RudeDark)
    ) {
        val (mainFrame, bottomPanel) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(mainFrame) {
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomPanel.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    // Adding vertical bias to center mainFrame in the available space
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomPanel.top)
                    verticalChainWeight = 0.5f
                }
        ) {
            MainFrame(
                modifier = Modifier
                    .size(width = 330.dp, height = 220.dp)
                    .dashedBorder(RectangleBorderColor, RoundedCornerShape(12.dp)),
                viewModel = viewModel
            )
        }

        Box(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.25f) // Set height to 1/4 of the screen
                }
        ) {
            BottomPanel(viewModel = viewModel)
        }
    }
}


@Composable
fun MainFrame(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    BoxWithConstraints(
        modifier = modifier
            .clipToBounds()
    ) {
        val parentWidthPx = constraints.maxWidth
        val parentHeightPx = constraints.maxHeight

        val imageWidth = 179.dp
        val imageHeight = 127.dp
        val imageWidthPx = with(LocalDensity.current) { imageWidth.toPx() }
        val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }

        val startX = ((parentWidthPx - imageWidthPx) / 2)
        val startY = ((parentHeightPx - imageHeightPx) / 2)

        viewModel.boxWidth.floatValue = parentWidthPx.toFloat()
        viewModel.boxHeight.floatValue = parentHeightPx.toFloat()
        viewModel.imageWidth.floatValue = imageWidthPx
        viewModel.imageHeight.floatValue = imageHeightPx

        viewModel.offset.value = Offset(startX, startY)


        FrameWithImage(
            modifier = Modifier
                .offset {
                    viewModel.offset.value.round()
                }, viewModel

        )
    }
}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Box(
        modifier = modifier
            .size(width = 179.dp, height = 127.dp)
            .border(
                BorderStroke(2.dp, RectangleBorderColor),
                RoundedCornerShape(8.dp)
            )
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))

    ) {
        viewModel.currentBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = viewModel.zoom.value
                        scaleY = viewModel.zoom.value
                        rotationZ = viewModel.angle.value
                    },
                contentDescription = "Image for edit",
                contentScale = ContentScale.Crop
            )
        } ?: run {

            Image(
                painter = ColorPainter(Color.Green),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentDescription = "Image for edit",
                contentScale = ContentScale.Crop

            )
        }

    }
}

@Composable
fun BottomPanel(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                color = RudeMid,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(16.dp)

    ) {
        TouchPanel(viewModel = viewModel)
    }
}

@Composable
fun TouchPanel(modifier: Modifier = Modifier, viewModel: MainViewModel) {
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
                        val oldScale = viewModel.zoom.value
                        val newScale =
                            (viewModel.zoom.value * gestureZoom).coerceIn(0.5f..5f)
                        if (oldScale == newScale) {
                            val summed = viewModel.offset.value + gesturePan
                            viewModel.updateOffset(summed)
                        } else {
                            viewModel.updateAngle(viewModel.angle.value + gestureRotate)
                            viewModel.updateZoom(newScale)
                        }

                    }
                )
            }
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
