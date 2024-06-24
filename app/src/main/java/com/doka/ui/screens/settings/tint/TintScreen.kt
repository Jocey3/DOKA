package com.doka.ui.screens.settings.tint

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
import com.doka.MainViewModel
import com.doka.R
import com.doka.ui.theme.ButtonBackgroundColor
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid
import com.doka.ui.theme.TextSimpleColor
import com.doka.util.changeTint
import com.doka.util.loadCompressedBitmap
import com.doka.util.textTintFormat

@Composable
fun TintScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: TintViewModel = hiltViewModel()
) {

    viewModel.tint.floatValue = sharedVM.tint.floatValue

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
                sharedVM
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
                sharedVM = sharedVM,
                navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }
    }
}

@Composable
fun MainFrame(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        FrameWithImage(modifier = Modifier.offset {
            Offset(
                sharedVM.savedImagesSettings.value.offsetX,
                sharedVM.savedImagesSettings.value.offsetY
            ).round()
        }, sharedVM = sharedVM)
    }
}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
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
                        scaleX = sharedVM.savedImagesSettings.value.zoom
                        scaleY = sharedVM.savedImagesSettings.value.zoom
                        rotationZ = sharedVM.savedImagesSettings.value.rotation
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
    viewModel: TintViewModel = hiltViewModel()
) {

    var tintDefault = remember { sharedVM.tint.floatValue }
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
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable {
                        sharedVM.currentBitmap = sharedVM.changedBitmap
                        sharedVM.tint.floatValue = tintDefault
                        navigateBack()
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Tint",
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
                        tintDefault = viewModel.tint.floatValue
                        sharedVM.tint.floatValue = viewModel.tint.floatValue
                        navigateNext()
                    }
            )
        }
        TintSlider(modifier = Modifier.weight(1f), sharedVM)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TintSlider(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: TintViewModel = hiltViewModel()
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
                if (viewModel.tint.floatValue > -1) {
                    viewModel.tint.floatValue -= 0.01f
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
                        text = textTintFormat(viewModel.tint.floatValue),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RudeDark
                    )
                }
            },
            valueRange = -1f..1f,
            value = viewModel.tint.floatValue,
            onValueChange = {
                if (it == -0.00f || (it in -0.01f..0.01f)) {
                    viewModel.tint.floatValue = 0f
                } else {
                    viewModel.tint.floatValue = it
                }
                changeBitmap(viewModel, sharedVM)
            }
        )

        Image(
            modifier = Modifier.clickable {
                if (viewModel.tint.floatValue < 1) {
                    viewModel.tint.floatValue += 0.01f
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
        TintScreen()
    }
}

fun changeBitmap(viewModel: TintViewModel, sharedVM: MainViewModel) {
    val originalBitmap = sharedVM.changedBitmap
    sharedVM.currentBitmap = originalBitmap?.let { bitmap ->
        changeTint(bitmap, viewModel.tint.floatValue)
    }
    sharedVM.tint.floatValue = viewModel.tint.floatValue
}