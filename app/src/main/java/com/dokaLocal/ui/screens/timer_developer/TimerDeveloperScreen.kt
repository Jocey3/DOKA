package com.dokaLocal.ui.screens.timer_developer

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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


@Composable
fun TimerDeveloperScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel(),
    viewModel: TimerDeveloperViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    viewModel.mediaPlayer = remember { MediaPlayer.create(context, R.raw.beep_sound) }
    viewModel.navigateNext = remember { navigateNext }

    LaunchedEffect(Unit) {
        viewModel.loadProgress()
    }

    BackHandler {
        navigateBack()
    }
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
                navigateNext = navigateNext,
                navigateBack = navigateBack
            )
        }

    }

}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateBack: () -> Unit = {},
    viewModel: TimerDeveloperViewModel = hiltViewModel()
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
            Text(
                modifier = Modifier.weight(1f),
                text = "Developer",
                color = TextSimpleColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Image(
                imageVector = ImageVector.vectorResource(id = if (viewModel.paused.value) R.drawable.svg_play else R.drawable.svg_pause),
                contentDescription = "Button pause",
                modifier = Modifier
                    .clickable {
                        if (viewModel.paused.value) {
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
fun Timer(modifier: Modifier = Modifier, viewModel: TimerDeveloperViewModel = hiltViewModel()) {
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
                if (targetCount > 60) (targetCount / 1000).toString() else targetCount.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ButtonBackgroundColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        TimerDeveloperScreen()
    }
}
