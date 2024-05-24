package com.doka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.doka.ui.source_picture.ImageSourceScreen
import com.doka.ui.theme.DOKATheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DOKATheme {
                NavigationComponent(navigator = Navigator())
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        ImageSourceScreen()
    }
}