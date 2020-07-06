package com.oyelekeokiki

import androidx.test.runner.AndroidJUnit4
import com.oyelekeokiki.helpers.ColorHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

//This is an Instrumented Test because ColorHelper resource won't be mocked

@RunWith(AndroidJUnit4::class)
class ColorsUtilTest {
    @Test
    fun testColorFromTextAlgorithm() {
        assertEquals(ColorHelper.generateColorFromText("ooo"), -4342340)
    }
}