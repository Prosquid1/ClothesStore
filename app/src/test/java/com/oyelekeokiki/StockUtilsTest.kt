package com.oyelekeokiki

import com.oyelekeokiki.helpers.NO_STOCK_INT
import com.oyelekeokiki.helpers.OUT_OF_STOCK_TEXT
import com.oyelekeokiki.helpers.StockCountHelper
import org.junit.Test

class StockUtilsTest {
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

    @Test
    fun testItemIsSoldOut() {
        assert(StockCountHelper.NONE.isItemSoldOut())
    }

    @Test
    fun testLowItemIsNotSoldOut() {
        assert(!StockCountHelper.LOW.isItemSoldOut())
    }

    @Test
    fun testMediumItemIsNotSoldOut() {
        assert(!StockCountHelper.MEDIUM.isItemSoldOut())
    }

    @Test
    fun testNoStockTextValue() {
        assert(StockCountHelper.getTextValue(NO_STOCK_INT) == OUT_OF_STOCK_TEXT)
    }

    @Test
    fun testStockAvailableTextValue() {
        assert(StockCountHelper.getTextValue(MockObject.LOW_STOCK_INT) == "(2 left)")
    }

    @Test
    fun testNoStockPriority() {
        assert(StockCountHelper.getPriority(NO_STOCK_INT) == StockCountHelper.NONE)
    }

    @Test
    fun testLowStockPriority() {
        assert(StockCountHelper.getPriority(MockObject.LOW_STOCK_INT) == StockCountHelper.LOW)
    }

    @Test
    fun testMediumStockPriority() {
        assert(StockCountHelper.getPriority(MockObject.MEDIUM_STOCK_INT) == StockCountHelper.MEDIUM)
    }
}