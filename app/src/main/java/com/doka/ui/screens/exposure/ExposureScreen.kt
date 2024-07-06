package com.doka.ui.screens.exposure

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.MainViewModel
import com.doka.R
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.FrameInnerColor
import com.doka.ui.theme.RedText
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid
import com.doka.util.ButtonDefault
import com.doka.util.negative
import kotlin.math.roundToInt


@Composable
fun ExposureScreen(
    modifier: Modifier = Modifier,
    navigateExpose: () -> Unit = {},
    navigateSettings: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: ExposureViewModel = hiltViewModel(),
) {
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
                .padding(top = 32.dp), sharedVM
        )

        BottomPanel(
            modifier = Modifier
                .constrainAs(bottomPanel) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.25f) // Set height to 1/4 of the screen
                },
            navigateExpose = navigateExpose,
            navigateSettings = navigateSettings,
            navigateBack = navigateBack, sharedVM = sharedVM
        )
    }
}

@Composable
fun MainFrame(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    BoxWithConstraints(
        modifier = modifier
            .clipToBounds()
    ) {
        val imageWidth = sharedVM.imageSize?.width ?: with(LocalDensity.current) { 179.dp.toPx() }
        val imageHeight =
            sharedVM.imageSize?.height ?: with(LocalDensity.current) { 127.dp.toPx() }

        val instructionWidth = with(LocalDensity.current) { 179.dp.toPx() }
        val instructionHeight = with(LocalDensity.current) { 127.dp.toPx() }

        val offsetXRange = 0f..(constraints.maxWidth.toFloat() - instructionWidth)
        val offsetYRange = 0f..(constraints.maxHeight.toFloat() - instructionHeight)

        val offsetXCalculation = if (imageWidth <= instructionWidth) {
            sharedVM.savedImagesSettings.value.offsetX - ((instructionWidth - imageWidth) / 2)
        } else {
            sharedVM.savedImagesSettings.value.offsetX + ((imageWidth - instructionWidth) / 2)
        }

        val offsetYCalculation = if (imageHeight <= instructionHeight) {
            sharedVM.savedImagesSettings.value.offsetY - ((instructionHeight - imageHeight) / 2)
        } else {
            sharedVM.savedImagesSettings.value.offsetY + ((imageHeight - instructionHeight) / 2)
        }

        val offsetX = offsetXCalculation.coerceIn(offsetXRange)
        val offsetY = offsetYCalculation.coerceIn(offsetYRange)

        Log.d("MyLog", "offsetX $offsetX offsetY $offsetY")
        Box(
            modifier = Modifier.offset {
                IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
            }) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 179.dp, height = 127.dp)
                    .background(FrameInnerColor)
                    .padding(2.dp)
                    .clip(RectangleShape)
            ) {
                Text(
                    text = "Place photo paper here",
                    fontSize = 20.sp,
                    color = RedText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 30.dp, horizontal = 20.dp)
                        .rotate(180f)
                )
            }
        }
    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateExpose: () -> Unit = {},
    navigateSettings: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = RudeMid,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 30.dp)

    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(
                    color = RudeMid,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_arrow_back_up),
                contentDescription = "Button back",
                modifier = Modifier
                    .clickable { navigateBack() }
            )
            Spacer(modifier = Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.svg_edit),
                contentDescription = "Button settings",
                modifier = Modifier
                    .clickable { navigateSettings() }

            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ButtonDefault(
            modifier = Modifier
                .fillMaxWidth(), text = "Expose"
        ) {
            sharedVM.beforeExposure = sharedVM.currentBitmap
            sharedVM.currentBitmap = sharedVM.currentBitmap?.negative()
            navigateExpose()
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        ExposureScreen()
    }
}
