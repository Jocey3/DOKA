package com.dokaLocal.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.dokaLocal.ImageSettings
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.RudeDark
import com.dokaLocal.ui.theme.RudeLight
import com.dokaLocal.ui.theme.RudeMid


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
        val (mainFrame, middle, bottomPanel) = createRefs()

        Spacer(modifier = Modifier
            .size(1.dp)
            .constrainAs(middle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })

        MainFrame(
            modifier = Modifier
                .constrainAs(mainFrame) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(middle.top)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(horizontal = 32.dp)
                .padding(top = 64.dp),
            sharedVM = sharedVM
        )

        BottomPanel(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.25f)// Set height to 1/4 of the screen
                },
            sharedVM = sharedVM, navigateNext = navigateNext,
            navigateBack = navigateBack
        )
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
        val density = LocalDensity.current
        LaunchedEffect(Unit) {
            viewModel.setFrameSize(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())

            val imageFrameWidth = constraints.maxWidth * 0.7f
            val imageFrameHeight = constraints.maxHeight * 0.7f
            val imageFrameWidthDp: Dp = with(density) { imageFrameWidth.toDp() }
            val imageFrameHeightDp: Dp = with(density) { imageFrameHeight.toDp() }

            viewModel.setImageFrameSize(
                imageFrameWidth,
                imageFrameHeight,
                imageFrameWidthDp,
                imageFrameHeightDp
            )

            sharedVM.currentBitmap?.let {
                viewModel.setRealImageSize(it.width, it.height, density)
            }
            viewModel.realImageSize.value?.let {
                val startX = ((constraints.maxWidth - it.width) / 2)
                val startY = ((constraints.maxHeight - it.height) / 2)

                if (viewModel.offset.value == null) viewModel.updateOffset(Offset(startX, startY))
            }
        }
        viewModel.realImageSize.value?.let { realImageSize ->
            realImageSize.widthDp?.let { widthDp ->
                realImageSize.heightDp?.let { heightDp ->
                    FrameWithImage(
                        modifier = Modifier
                            .size(width = widthDp, height = heightDp)
                            .offset {
                                viewModel.offset.value?.round() ?: Offset(0f, 0f).round()
                            }, sharedVM

                    )
                }
            }

        }
    }
}

@Composable
fun FrameWithImage(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: EditViewModel = hiltViewModel()
) {
    Box(modifier = modifier) {
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
                contentScale = ContentScale.Fit
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
        Column {
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
                        sharedVM.imageSize = viewModel.realImageSize.value
                        navigateNext()
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_rotation),
                contentDescription = "Rotate",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        viewModel.updateAngle()
                    }
            )
        }

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
                    onGesture = { _, gesturePan, gestureZoom, gestureRotate ->
                        val oldScale = viewModel.zoom.value
                        val newScale =
                            (viewModel.zoom.value * gestureZoom).coerceIn(0.5f..5f)
                        if (oldScale == newScale) {
                            val summed = viewModel.offset.value!! + gesturePan
                            viewModel.updateOffset(summed)
                        } else {
//                            viewModel.updateAngle(viewModel.angle.value + gestureRotate)
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

@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=411dp,height=901dp,dpi=420"
)
@Composable
fun EditScreenPreview() {
    DOKATheme {
        EditScreen()
    }
}
