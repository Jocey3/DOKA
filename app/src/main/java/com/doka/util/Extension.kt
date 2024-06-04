package com.doka.util

import android.app.Activity
import android.content.Context.AUDIO_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.view.WindowManager
import okhttp3.ResponseBody

fun Activity.setAppSettings() {
    val am = getSystemService(AUDIO_SERVICE) as AudioManager

    am.setStreamVolume(
        AudioManager.STREAM_MUSIC,
        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
        0
    )

    val lp = window.attributes
    lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
    window.attributes = lp
}

fun ResponseBody.getBitmap(): Bitmap {
    return BitmapFactory.decodeStream(this.byteStream())
}