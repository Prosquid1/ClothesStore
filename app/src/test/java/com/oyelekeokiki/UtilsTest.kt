package com.oyelekeokiki

import com.oyelekeokiki.helpers.StockCountHelper
import com.oyelekeokiki.helpers.formatPrice
import org.junit.Test

class UtilsTest {
    @Test
    fun `format Price`() {
        assert(3.formatPrice() == "£3")
    }

    @Test
    fun `format Price Fail`() {
        assert(0.formatPrice() != "0")
    }

    @Test
    fun testNoStockColor() {
        assert(StockCountHelper.NONE.getColor() == R.color.red)
    }

    @Test
    fun testLowStockColor() {
        assert(StockCountHelper.LOW.getColor() == R.color.green)
    }

    @Test
    fun testMediumStockColor() {
        assert(StockCountHelper.MEDIUM.getColor() == R.color.money_blue)
    }
}