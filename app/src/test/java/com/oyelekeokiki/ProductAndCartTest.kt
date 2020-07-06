package com.oyelekeokiki

import com.oyelekeokiki.utils.StockCountHelper
import org.junit.Test

class ProductAndCartTest {
    @Test
    fun testNoStockColor() {
        assert(StockCountHelper.NONE.getColor() == R.color.red)
    }
}