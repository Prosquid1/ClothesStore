package com.oyelekeokiki.helpers

import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.oyelekeokiki.helpers.configureCSRecycler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

@RunWith(AndroidJUnit4::class)
class RecyclerConfigExtTest {

    lateinit var recyclerView: RecyclerView

    @Before
    fun setup() {
        recyclerView = RecyclerView(InstrumentationRegistry.getInstrumentation().context)
        recyclerView.configureCSRecycler()
    }
    @Test
    fun recyclerHasItemDecorator()  {
        assertEquals(recyclerView.itemDecorationCount, 1)
    }

    @Test
    fun recyclerHasLayoutManager()  {
        assertNotEquals(recyclerView.layoutManager, null)
    }

    @Test
    fun recyclerHasItemAnimator()  {
        assertNotEquals(recyclerView.itemAnimator, null)
    }
}