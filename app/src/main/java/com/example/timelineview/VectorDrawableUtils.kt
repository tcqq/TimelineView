package com.example.timelineview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Created by Vipul Asri on 28/12/16.
 */
object VectorDrawableUtils {

    fun getDrawable(
        context: Context,
        @DrawableRes drawableRes: Int
    ): Drawable? {
        return VectorDrawableCompat.create(context.resources, drawableRes, context.theme)
    }

    fun getDrawable(
        context: Context,
        @DrawableRes drawableRes: Int,
        @ColorInt color: Int
    ): Drawable {
        val drawable = getDrawable(context, drawableRes)!!
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun getBitmap(
        context: Context,
        @DrawableRes drawableRes: Int
    ): Bitmap {
        val drawable = getDrawable(context, drawableRes)

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
