package com.dokaLocal.ui.screens.splash

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.dokaLocal.MainViewModel
import com.dokaLocal.ui.theme.DOKATheme
import com.dokaLocal.ui.theme.RudeDark
import com.dokaLocal.ui.theme.RudeMid
import com.dokaLocal.util.ButtonDefault
import com.dokaLocal.util.adjustedImage
import java.io.IOException


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit = {},
    navigateEdit: () -> Unit = {},
    sharedVM: MainViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as? Activity

    BackHandler {
        activity?.finish()
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
                sharedVM = sharedVM,
                navigateNext = navigateNext,
                navigateEdit = navigateEdit
            )
        }

    }

}

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    sharedVM: MainViewModel = hiltViewModel(),
    navigateNext: () -> Unit = {},
    navigateEdit: () -> Unit = {}
) {
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        try {
            val inputStream = uri?.let { context.contentResolver?.openInputStream(it) }
            if (inputStream != null) {
                try {
                    val inputStream = uri.let { context.contentResolver?.openInputStream(it) }
                    if (inputStream != null) {
                        sharedVM.currentBitmap =
                            BitmapFactory.decodeStream(inputStream).adjustedImage()
                        navigateEdit.invoke()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = RudeMid,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 30.dp)

    ) {
        Spacer(modifier = Modifier.weight(1f))
        ButtonDefault(modifier = Modifier.fillMaxWidth(), text = "Load image") {
            sharedVM.clearData()
            navigateNext()
        }
        Spacer(modifier = Modifier.weight(1f))
        ButtonDefault(text = "Select Photo") {
            sharedVM.clearData()
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        SplashScreen()
    }
}
