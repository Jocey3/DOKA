package com.doka.ui.screens.exposure

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.MainViewModel
import com.doka.R
import com.doka.ui.screens.edit.dashedBorder
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.FrameInnerColor
import com.doka.ui.theme.RectangleBorderColor
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid
import com.doka.util.ButtonDefault
import com.doka.util.negative


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
                    .dashedBorder(RectangleBorderColor, RoundedCornerShape(12.dp))
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
                navigateExpose = navigateExpose,
                navigateSettings = navigateSettings,
                navigateBack = navigateBack, sharedVM = sharedVM
            )
        }

    }

}

@Composable
fun MainFrame(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clipToBounds()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 179.dp, height = 127.dp)
                .border(
                    BorderStroke(2.dp, RectangleBorderColor),
                    RoundedCornerShape(8.dp)
                )
                .background(FrameInnerColor)
                .padding(2.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Place photo paper here",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 20.dp)
                    .rotate(180f)
            )
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
