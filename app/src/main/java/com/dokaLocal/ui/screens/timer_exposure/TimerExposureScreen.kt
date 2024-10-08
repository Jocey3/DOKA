package com.dokaLocal.ui.screens.timer_exposure

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.dokaLocal.ui.theme.BottomPanelColor
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.MainBackgroundColor
import com.dokaLocal.ui.theme.TextSimpleColor
import com.dokaLocal.util.rotate
import kotlinx.coroutines.delay


@Composable
fun TimerExposureScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: TimerExposureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.timeLeft.value = sharedVM.timeForExposure.floatValue.toLong()
        viewModel.maxTime.value = sharedVM.timeForExposure.floatValue.toLong() * 1000L
        delay(1000)
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
            .background(MainBackgroundColor)
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
}

@Composable
fun FrameWithImage(modifier: Modifier = Modifier, sharedVM: MainViewModel) {
    Box(modifier = modifier) {
        val image =
            remember { sharedVM.currentBitmap!!.rotate(sharedVM.savedImagesSettings.value.rotation) }

        Image(
            bitmap = image.asImageBitmap(),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = sharedVM.savedImagesSettings.value.zoom
                    scaleY = sharedVM.savedImagesSettings.value.zoom
                },
            contentDescription = "Image for edit",
            contentScale = ContentScale.Fit
        )

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
                color = BottomPanelColor,
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
                    color = BottomPanelColor,
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
        viewModel.timeLeft.value?.let {
            AnimatedContent(
                targetState = it,
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
                val digits =
                    if (targetCount > 90) (targetCount / 1000).toInt() else targetCount.toInt()
                val digitList = digits.toString().map { num -> num.toString().toInt() }
                Row {
                    for (digit in digitList) {
                        val drawableId = when (digit) {
                            0 -> R.drawable.svg_digit_0
                            1 -> R.drawable.svg_digit_1
                            2 -> R.drawable.svg_digit_2
                            3 -> R.drawable.svg_digit_3
                            4 -> R.drawable.svg_digit_4
                            5 -> R.drawable.svg_digit_5
                            6 -> R.drawable.svg_digit_6
                            7 -> R.drawable.svg_digit_7
                            8 -> R.drawable.svg_digit_8
                            9 -> R.drawable.svg_digit_9
                            else -> R.drawable.svg_digit_0
                        }
                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = "Digit $digit",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(86.dp)
                        )
                    }
                }
            }
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
