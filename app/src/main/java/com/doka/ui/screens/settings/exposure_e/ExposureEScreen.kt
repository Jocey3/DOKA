package com.doka.ui.screens.settings.exposure_e


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
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
import com.doka.MainViewModel
import com.doka.R
import com.doka.ui.theme.ButtonBackgroundColor
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid
import com.doka.ui.theme.TextSimpleColor
import com.doka.util.changeExposure
import com.doka.util.loadCompressedBitmap


@Composable
fun ExposureEScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: ExposureEViewModel = hiltViewModel()
) {
    viewModel.exposure.floatValue = sharedVM.exposure.floatValue

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
                    width = sharedVM.imageSizeDp?.widthDp!!,
                    height = sharedVM.imageSizeDp?.heightDp!!
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
        }
    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel,
    viewModel: ExposureEViewModel = hiltViewModel()
) {
    var exposureDefault = remember { sharedVM.exposure.floatValue }
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
                        sharedVM.exposure.floatValue = exposureDefault
                        navigateBack()
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Exposure",
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
                        exposureDefault = viewModel.exposure.floatValue
                        sharedVM.exposure.floatValue = viewModel.exposure.floatValue
                        navigateNext()
                    }
            )
        }
        ExposureESlider(modifier = Modifier.weight(1f), sharedVM)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposureESlider(
    modifier: Modifier = Modifier, sharedVM: MainViewModel,
    viewModel: ExposureEViewModel = hiltViewModel()
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
                if (viewModel.exposure.floatValue > 0) {
                    viewModel.exposure.floatValue -= 0.01f
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
                        text = String.format("%.2f", viewModel.exposure.floatValue),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RudeDark
                    )
                }
            },
            valueRange = 0f..2f,
            value = viewModel.exposure.floatValue,
            onValueChange = {
                viewModel.exposure.floatValue = it
                changeBitmap(viewModel, sharedVM)
            }
        )

        Image(
            modifier = Modifier.clickable {
                if (viewModel.exposure.floatValue < 2) {
                    viewModel.exposure.floatValue += 0.01f
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
        ExposureEScreen()
    }
}

fun changeBitmap(viewModel: ExposureEViewModel, sharedVM: MainViewModel) {
    val originalBitmap = sharedVM.changedBitmap
    sharedVM.currentBitmap = originalBitmap?.let { bitmap ->
        changeExposure(bitmap, viewModel.exposure.floatValue)
    }
    sharedVM.exposure.floatValue = viewModel.exposure.floatValue
}
