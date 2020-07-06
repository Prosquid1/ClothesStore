package com.oyelekeokiki


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.oyelekeokiki.database.AppDataBase
import com.oyelekeokiki.database.WishListDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * Ideally, Database tests should be executed on an Android device.
 * See [testing documentation](https://developer.android.com/training/data-storage/room/testing-db).
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private var wishListDao: WishListDao? = null
    private var appDataBase: AppDataBase? = null

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() //Required for getOrAwaitValue function

    private val product = MockObject.provideSingleTestProduct()

    @Before
    fun setup() {
        appDataBase = Room
            .inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                AppDataBase::class.java
            )
            .build()
        wishListDao = appDataBase?.wishListDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        appDataBase?.close();
    }

    @Test
    fun shouldDeleteAll() = runBlocking {
        wishListDao?.deleteAll()
        assertEquals(wishListDao?.getWishListCount()?.getOrAwaitValue(), 0)
    }

    @Test
    fun shouldInsertProduct() = runBlocking {
        wishListDao?.addToWishList(product)
        val productTest = wishListDao?.getWishListItemWith(product.id)
        assertEquals(product.id, productTest?.id)
    }

    @Test
    fun shouldDeleteProduct() = runBlocking {
        wishListDao?.removeFromWishList(product.id)
        val productTest = wishListDao?.getWishListItemWith(product.id)
        assertNull(productTest)
    }

    @Test
    fun shouldInsertAll() = runBlocking {
        val listOfNewProducts = MockObject.provideTestProducts()
        wishListDao?.insertWishListProducts(listOfNewProducts)

        val allNewlyAddedProducts = wishListDao?.getWishList()?.getOrAwaitValue()
        assertEquals(allNewlyAddedProducts?.size, listOfNewProducts.size)
    }

    @Test
    fun databaseIsAtomic() = runBlocking {
        val listOfNewProducts = listOf(product, product, product, product)
        wishListDao?.deleteAll()
        wishListDao?.insertWishListProducts(listOfNewProducts)

        val allNewlyAddedProducts = wishListDao?.getWishList()?.getOrAwaitValue()
        assertEquals(allNewlyAddedProducts?.size, 1)
    }
}

// Extension functions
private fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}