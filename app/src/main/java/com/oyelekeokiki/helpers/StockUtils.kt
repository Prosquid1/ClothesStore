package com.oyelekeokiki.helpers

import com.oyelekeokiki.R

enum class StockCountHelper {
    NONE, LOW, MEDIUM;

    /**
     * Helper to setup [ProductCartViewHolder]
     * **/
    companion object {
        fun getDimAlphaValue(count: Int) = when (count) {
            NO_STOCK_INT -> SOLD_OUT_ITEM_ALPHA
            else -> AVAILABLE_ITEM_ALPHA
        }

        fun getTextValue(count: Int): String = when (count) {
            NO_STOCK_INT -> OUT_OF_STOCK_TEXT
            else -> "(${count} left)"
        }

        fun getPriority(count: Int): StockCountHelper {
            return when {
                count == NO_STOCK_INT -> NONE
                count <= MINIMUM_STOCK_THRESHOLD -> LOW
                else -> MEDIUM
            }
        }
    }

    fun getColor() = when (this) {
        NONE -> R.color.red
        LOW -> R.color.green
        MEDIUM -> R.color.money_blue
    }

    fun isItemSoldOut() = when (this) {
        NONE -> true
        else -> false
    }
}


