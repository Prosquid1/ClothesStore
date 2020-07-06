package com.oyelekeokiki

import com.oyelekeokiki.helpers.StockCountHelper
import org.junit.Test

class ProductAndCartTest {
    @Test
    fun testNoStockColor() {
        assert(StockCountHelper.NONE.getColor() == R.color.red)
    }
}