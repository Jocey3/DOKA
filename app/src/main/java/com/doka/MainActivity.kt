package com.doka

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.doka.ui.screens.source_picture.ImageSourceScreen
import com.doka.ui.theme.DOKATheme
import com.doka.util.setAppSettings
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppSettings()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the app has permission to modify Do Not Disturb settings
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            showDNDSettingsPrompt()
        }

        enableEdgeToEdge()
        setContent {
            DOKATheme {
                NavigationComponent(navigator = Navigator())
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // Enable Do Not Disturb mode
        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
        }
    }

    override fun onStop() {
        // Disable Do Not Disturb mode
        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
        super.onStop()
    }

    override fun onDestroy() {
        // Disable Do Not Disturb mode
        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
        super.onDestroy()
    }

    private fun showDNDSettingsPrompt() {
        AlertDialog.Builder(this)
            .setTitle("Configure Do Not Disturb Settings")
            .setMessage("Please adjust your Do Not Disturb settings.")
            .setPositiveButton("OK") { dialog, _ ->
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
                dialog.dismiss()
            }
            .show()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    DOKATheme {
        ImageSourceScreen()
    }
}