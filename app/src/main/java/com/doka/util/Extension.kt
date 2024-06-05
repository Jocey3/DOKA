package com.doka.util

import android.app.Activity
import android.content.Context.AUDIO_SERVICE
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
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

fun Bitmap.adjustedImage(): Bitmap {
    return flippedHorizontally().noir()
}

fun Bitmap.flippedHorizontally(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.noir(): Bitmap {
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(cm)
    val bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888)
    val c = Canvas(bmpGrayscale)
    val paintDraw = Paint()
    c.drawBitmap(this, 0f, 0f, paintDraw)
    c.drawBitmap(bmpGrayscale, 0f, 0f, paint)
    return bmpGrayscale
}

fun Bitmap.negative(): Bitmap {
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getPixels(pixels, 0, width, 0, 0, width, height)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val index = y * width + x
            var pixel = pixels[index]

            val alpha = Color.alpha(pixel)
            var red = 255 - Color.red(pixel)
            var green = 255 - Color.green(pixel)
            var blue = 255 - Color.blue(pixel)

            pixel = Color.argb(alpha, red, green, blue)
            pixels[index] = pixel
        }
    }

    return Bitmap.createBitmap(pixels, width, height, Config.ARGB_8888)
}
