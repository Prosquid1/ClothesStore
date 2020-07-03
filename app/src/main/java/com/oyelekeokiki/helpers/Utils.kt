package com.oyelekeokiki.helpers

import android.graphics.Color
import com.oyelekeokiki.R
import java.util.*

object ColorHelper {
    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    fun getStockLowColor() {

    }

}

enum class StockCountPriority {
    LOW, MEDIUM;

    fun getColor() = when (this) {
        LOW -> R.color.colorAccent
        MEDIUM -> R.color.colorPrimary
    }
}