package com.oyelekeokiki

import androidx.test.runner.AndroidJUnit4
import com.oyelekeokiki.utils.ColorHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

//This is an Instrumented Test because ColorHelper resource won't be mocked

@RunWith(AndroidJUnit4::class)
class ColorsUtilTest {

    @Test
    fun testModifiedHexIntPass() {
        assertEquals(ColorHelper.getModifiedHexInt('A'), 130)
    }

    @Test
    fun testModifiedHexIntFail() {
        assertNotEquals(ColorHelper.getModifiedHexInt('A'), -4342340)
    }

    @Test
    fun testColorFromTextPass() {
        assertEquals(ColorHelper.generateColorFromText("ooo"), -4342340)
    }

    @Test
    fun testColorFromTextFail() {
        assertNotEquals(ColorHelper.generateColorFromText("ooo"), -340)
    }
}