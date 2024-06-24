package com.doka.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.ImageSettings
import com.doka.MainViewModel
import com.doka.R
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeLight
import com.doka.ui.theme.RudeMid


@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
) {
    BackHandler {
        navigateBack()
    }

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
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 32.dp)
        ) {
            MainFrame(
                modifier = Modifier
                    .size(width = 330.dp, height = 220.dp),
                sharedVM = sharedVM
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
            BottomPanel(
                sharedVM = sharedVM, navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }
    }
}


@Composable
fun MainFrame(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
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

        if (viewModel.offset.value == null) viewModel.updateOffset(Offset(startX, startY))

        FrameWithImage(
            modifier = Modifier
                .offset {
                    viewModel.offset.value!!.round()
                }, sharedVM

        )
    }
}

@Composable
fun FrameWithImage(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .size(width = 179.dp, height = 127.dp)
            .padding(2.dp)
    ) {
        sharedVM.currentBitmap?.let {
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
                contentScale = ContentScale.FillBounds
            )
        } ?: run {

            Image(
                painter = ColorPainter(Color.Green),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentDescription = "Image for edit",
                contentScale = ContentScale.FillBounds

            )
        }

    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = RudeMid,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 30.dp)

    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
            contentDescription = "Button back",
            modifier = Modifier
                .clickable { navigateBack() }

        )
        Spacer(modifier = Modifier.width(16.dp))
        TouchPanel(modifier = Modifier.weight(1f), sharedVM = sharedVM)
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.svg_check),
            contentDescription = "Button Next",
            modifier = Modifier
                .clickable {
                    sharedVM.savedImagesSettings.value = ImageSettings(
                        viewModel.zoom.value,
                        viewModel.angle.value,
                        viewModel.offset.value!!.x,
                        viewModel.offset.value!!.y
                    )
                    navigateNext()
                }
        )
    }
}

@Composable
fun TouchPanel(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
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
                            val summed = viewModel.offset.value!! + gesturePan
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
