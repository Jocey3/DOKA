package com.doka.util

import android.app.Activity
import android.media.AudioManager
import android.view.WindowManager
import androidx.activity.ComponentActivity

fun Activity.setAppSettings() {
    val am = getSystemService(ComponentActivity.AUDIO_SERVICE) as AudioManager

    am.setStreamVolume(
        AudioManager.STREAM_MUSIC,
        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
        0
    )

    val lp = window.attributes
    lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
    window.attributes = lp
}