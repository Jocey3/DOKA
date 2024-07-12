package com.dokaLocal.ui.screens.settings.contrast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.ui.theme.ButtonBackgroundColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.RudeDark
import com.dokaLocal.ui.theme.RudeMid
import com.dokaLocal.ui.theme.TextSimpleColor
import com.dokaLocal.util.changeContrast
import com.dokaLocal.util.loadCompressedBitmap

@Composable
fun ContrastScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: ContrastViewModel = hiltViewModel()
) {
    viewModel.contrast.floatValue = sharedVM.contrast.floatValue

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
                bottom.linkTo(bottomPanel.top)
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
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp),
            sharedVM = sharedVM
        )

        BottomPanel(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.25f) // Set height to 1/4 of the screen
                },
            sharedVM = sharedVM,
            navigateNext = navigateNext,
            navigateBack = navigateBack
        )
    }
}

@Composable
fun MainFrame(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        FrameWithImage(
            modifier = Modifier
                .size(
                    width = sharedVM.imageSize?.widthDp!!,
                    height = sharedVM.imageSize?.heightDp!!
                )
                .offset {
                    Offset(
                        sharedVM.savedImagesSettings.value.offsetX,
                        sharedVM.savedImagesSettings.value.offsetY
                    ).round()
                }, sharedVM = sharedVM
        )
    }
}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    Box(modifier = modifier) {
        sharedVM.currentBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = sharedVM.savedImagesSettings.value.zoom
                        scaleY = sharedVM.savedImagesSettings.value.zoom
                        rotationZ = sharedVM.savedImagesSettings.value.rotation
                    },
                contentDescription = "Image for edit",
                contentScale = ContentScale.Fit
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
    viewModel: ContrastViewModel = hiltViewModel()
) {

    var contrastDefault = remember { sharedVM.contrast.floatValue }
    sharedVM.currentBitmap = sharedVM.currentBitmap?.let { loadCompressedBitmap(it) }
    sharedVM.changedBitmap = remember { sharedVM.currentBitmap }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = RudeMid,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 30.dp)
    ) {
        Row() {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable {
                        sharedVM.currentBitmap = sharedVM.changedBitmap
                        sharedVM.contrast.floatValue = contrastDefault
                        navigateBack()
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Contrast",
                color = TextSimpleColor,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_check),
                contentDescription = "Button Next",
                modifier = Modifier
                    .clickable {
                        sharedVM.changedBitmap = sharedVM.currentBitmap
                        contrastDefault = viewModel.contrast.floatValue
                        sharedVM.contrast.floatValue = viewModel.contrast.floatValue
                        navigateNext()
                    }
            )
        }
        ContrastSlider(modifier = Modifier.weight(1f), sharedVM)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContrastSlider(
    modifier: Modifier = Modifier, sharedVM: MainViewModel,
    viewModel: ContrastViewModel = hiltViewModel()
) {
    val colors = SliderDefaults.colors(
        thumbColor = ButtonBackgroundColor,
        activeTrackColor = TextSimpleColor,
        inactiveTrackColor = TextSimpleColor,
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.clickable {
                if (viewModel.contrast.floatValue > 0) {
                    viewModel.contrast.floatValue -= 0.01f
                    changeBitmap(viewModel, sharedVM)
                }
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.svg_minus),
            contentDescription = "Minus"
        )
        Slider(
            modifier = Modifier.weight(1f),
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier
                        .scale(scaleX = 1f, scaleY = 2f),
                    sliderState = sliderState, colors = colors
                )
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(ButtonBackgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%.2f", viewModel.contrast.floatValue),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RudeDark
                    )
                }
            },
            valueRange = 0f..2f,
            value = viewModel.contrast.floatValue,
            onValueChange = {
                viewModel.contrast.floatValue = it
                changeBitmap(viewModel, sharedVM)
            }
        )

        Image(
            modifier = Modifier.clickable {
                if (viewModel.contrast.floatValue < 2) {
                    viewModel.contrast.floatValue += 0.01f
                    changeBitmap(viewModel, sharedVM)
                }
            },
            imageVector = ImageVector.vectorResource(id = R.drawable.svg_plus),
            contentDescription = "Plus"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditScreenPreview() {
    DOKATheme {
        ContrastScreen()
    }
}

fun changeBitmap(viewModel: ContrastViewModel, sharedVM: MainViewModel) {
    val originalBitmap = sharedVM.changedBitmap
    sharedVM.currentBitmap = originalBitmap?.let { bitmap ->
        changeContrast(bitmap, viewModel.contrast.floatValue)
    }
    sharedVM.contrast.floatValue = viewModel.contrast.floatValue
}