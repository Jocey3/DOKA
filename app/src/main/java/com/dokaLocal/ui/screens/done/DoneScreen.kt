package com.dokaLocal.ui.screens.done

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.R
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.DefaultColor
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.util.ButtonDefault


@Composable
fun DoneScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    navigateNext: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
) {
    BackHandler {
        navigateBack()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
    ) {
        val (doneView, bottomPanel) = createRefs()
        Box(modifier = Modifier
            .constrainAs(doneView) {
                top.linkTo(parent.top)
                bottom.linkTo(bottomPanel.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                // Adding vertical bias to center mainFrame in the available space
                top.linkTo(parent.top)
                bottom.linkTo(bottomPanel.top)
                verticalChainWeight = 0.5f
            }) {
            DoneView()
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
            BottomPanel(sharedVM = sharedVM) { navigateNext() }
        }

    }

}

@Composable
fun DoneView(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier.size(164.dp),
            imageVector = ImageVector.vectorResource(R.drawable.svg_done),
            contentDescription = "Done",
        )

    }
}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    navigateNext: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = BottomPanelColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp)

    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 40.dp),
            text = "Wash photo for 2 minutes",
            fontSize = 18.sp,
            color = DefaultColor,
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonDefault(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 30.dp),
            text = "Select new photo"
        ) {
            sharedVM.clearData()
            navigateNext()
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=411dp,height=901dp,dpi=420"
)
@Composable
fun GreetingPreview() {
    DOKATheme {
        DoneScreen()
    }
}
