package com.example.timelineview

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt

/**
 * @author Alan Perry
 * @since 2018/07/22 Created
 */
object ThemeUtils {

    fun getThemeValue(resId: Int, context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(resId, value, true)
        return value.data
    }
}