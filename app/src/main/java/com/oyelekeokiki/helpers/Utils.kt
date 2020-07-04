package com.oyelekeokiki.helpers

import android.graphics.Color
import com.oyelekeokiki.R
import java.util.*

object ColorHelper {
    private fun getModifiedHexInt(char: Char): Int {
        val charInt = char.toInt()
        val randomizingSeed = if (charInt % 2 == 0) 1 else 2
        return charInt * randomizingSeed
    }
    fun generateColorFromText(string: String): Int {
        val mutatedString = string.toLowerCase(Locale.ROOT).convertToNCharacters(3)

        //Randomizing the colors since hex characters from strings are so close, they produce almost the same hue
        val colorArray = mutatedString.map { getModifiedHexInt(it) }

        // Randomizing again to spark up the colors!...
        val red = colorArray[0] + colorArray[1]
        val green = colorArray[1] + colorArray[2]
        val blue = colorArray[2] + colorArray[0]

        return Color.rgb(red, green, blue)
    }
}

fun String.convertToNCharacters(n: Int, padChar: Char = '.'): String {
    if (this.length >= n) {
        return this.take(n)
    }
    return this.padEnd(n - this.length, padChar)
}

enum class StockCountPriority {
    LOW, MEDIUM;

    fun getColor() = when (this) {
        LOW -> R.color.red
        MEDIUM -> R.color.green
    }
}