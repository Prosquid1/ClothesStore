package com.oyelekeokiki.helpers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.shared.CSItemAnimator
import kotlinx.android.synthetic.main.fragment_home.*
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

fun RecyclerView.configureCSRecycler() {
    val itemDecoration = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )
    context?.let {
        itemDecoration.setDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    it,
                    R.color.lightcolorPrimary
                )
            )
        )
    }
    addItemDecoration(
        itemDecoration
    )
    itemAnimator = CSItemAnimator()
    layoutManager = LinearLayoutManager(context)
}

fun List<Product>.getProductsInIDsList(productIds: List<Int>): List<Product> {
    return productIds.flatMap { mappedId -> this.filter { mappedId == it.id }}
}

enum class StockCountPriority {
    LOW, MEDIUM;

    fun getColor() = when (this) {
        LOW -> R.color.red
        MEDIUM -> R.color.money_blue
    }
}

enum class ActionResponseType {
    SUCCESS, ERROR;
}