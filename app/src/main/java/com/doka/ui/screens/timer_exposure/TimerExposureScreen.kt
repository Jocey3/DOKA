package com.doka.ui.screens.timer_exposure

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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


@Composable
fun TimerExposureScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: TimerExposureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(sharedVM.timeForExposure.value) {
        viewModel.maxTime.value = sharedVM.timeForExposure.value.toLong() * 1000L
        viewModel.loadProgress()
    }
    viewModel.mediaPlayer = remember { MediaPlayer.create(context, R.raw.beep_sound) }
    viewModel.navigateNext = remember { navigateNext }

    BackHandler {
        navigateBack()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(RudeDark)
            .systemBarsPadding()
            .padding(top = 32.dp)
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
                .padding(horizontal = 16.dp),
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
            navigateNext = navigateNext,
            navigateBack = navigateBack
        )

    }

}

@Composable
fun MainFrame(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel,
    viewModel: TimerExposureViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        if (viewModel.mainFrameVisible) {
            FrameWithImage(
                modifier = Modifier
                    .offset {
                        Offset(
                            sharedVM.savedImagesSettings.value.offsetX,
                            sharedVM.savedImagesSettings.value.offsetY
                        ).round()
                    }, sharedVM = sharedVM
            )
        }
    }
}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    Box(
        modifier = modifier
            .size(width = 179.dp, height = 127.dp)
            .padding(2.dp)
    ) {
        val image = remember { sharedVM.currentBitmap }

        image?.let {
            val isVertical = it.width < it.height
            val contentScale = if (isVertical) ContentScale.Fit else ContentScale.FillBounds

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
                contentScale = contentScale
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
    navigateBack: () -> Unit = {}
) {
    val viewModel: TimerExposureViewModel = hiltViewModel()
    val isPaused by viewModel.paused

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
            Text(
                modifier = Modifier.weight(1f),
                text = "Expose",
                color = TextSimpleColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Image(
                imageVector = ImageVector.vectorResource(id = if (isPaused) R.drawable.svg_play else R.drawable.svg_pause),
                contentDescription = "Button pause",
                modifier = Modifier
                    .clickable {
                        if (isPaused) {
                            viewModel.resumeTimer()
                        } else {
                            viewModel.pauseTimer()
                        }
                    }

            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Timer()
        Spacer(modifier = Modifier.weight(1f))
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun Timer(modifier: Modifier = Modifier, viewModel: TimerExposureViewModel = hiltViewModel()) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedContent(
            targetState = viewModel.timeLeft.value,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> -height } + fadeOut())
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> height } + fadeOut())
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }, label = "Timer"
        ) { targetCount ->
            Text(
                text = if (targetCount > 30) String.format(
                    "%d",
                    targetCount / 1000
                ) else String.format("%d", targetCount),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ButtonBackgroundColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimerExposurePreview() {
    DOKATheme {
        TimerExposureScreen()
    }
}
