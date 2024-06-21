package com.doka.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.doka.R
import com.doka.ui.theme.ButtonBackgroundColor
import com.doka.ui.theme.DOKATheme
import com.doka.ui.theme.RudeDark
import com.doka.ui.theme.RudeMid


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateExpTimer: () -> Unit = {},
    navigateExposure: () -> Unit = {},
    navigateSaturation: () -> Unit = {},
    navigateContrast: () -> Unit = {},
    navigateTint: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(RudeDark)
    ) {
        val (bottomPanel) = createRefs()
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
                navigateExpTimer = navigateExpTimer,
                navigateBack = navigateBack,
                navigateExposure = navigateExposure,
                navigateSaturation = navigateSaturation,
                navigateContrast = navigateContrast,
                navigateTint = navigateTint)
        }
    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateExpTimer: () -> Unit,
    navigateExposure: () -> Unit,
    navigateSaturation: () -> Unit,
    navigateContrast: () -> Unit,
    navigateTint: () -> Unit,
    navigateBack: () -> Unit = {}
) {
    val buttonList = remember {listOf(
        ButtonModel("Exp. timer", R.drawable.svg_timer),
        ButtonModel("Exposure", R.drawable.svg_exposure),
        ButtonModel("Saturation", R.drawable.svg_saturation),
        ButtonModel("Contrast", R.drawable.svg_contrast),
        ButtonModel("Tint", R.drawable.svg_tint)
    )}
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
            Spacer(modifier = Modifier.height(32.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(buttonList) { index, item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(70.dp)
                        .clickable {
                            if (buttonList[index].title == "Exp. timer") navigateExpTimer()
                            if (buttonList[index].title == "Exposure") navigateExposure()
                            if (buttonList[index].title == "Saturation") navigateSaturation()
                            if (buttonList[index].title == "Contrast") navigateContrast()
                            if (buttonList[index].title == "Tint") navigateTint()
                        }
                ) {
                    Image(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        imageVector = ImageVector.vectorResource(id = buttonList[index].image),
                        contentDescription = buttonList[index].title,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = buttonList[index].title,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = ButtonBackgroundColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}

data class ButtonModel(val title: String, val image: Int)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        SettingsScreen()
    }
}
