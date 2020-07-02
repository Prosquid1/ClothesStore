package com.oyelekeokiki


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.oyelekeokiki.database.AppDataBase
import com.oyelekeokiki.database.WishListDao
import com.oyelekeokiki.model.Product
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
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private var wishListDao: WishListDao? = null
    private var appDataBase: AppDataBase? = null

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val product = Product(1, "Test Product", "Test Description", oldPrice = "2.00", price = "1.89", stock = 2);

    @Before
    fun setup() {
        appDataBase = Room
            .inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        wishListDao = appDataBase?.wishListDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        appDataBase?.close();
    }

    @Test
    fun shouldDeleteAll(){
        wishListDao?.deleteAll()
        assertEquals(wishListDao?.getProductsCount(), 0)
    }

    @Test
    fun shouldInsertProduct() {
        wishListDao?.addToWishList(product)
        val productTest = wishListDao?.getWishListItemWith(product.id)
        assertEquals(product.id, productTest?.getOrAwaitValue()?.id)
    }

    @Test
    fun shouldDeleteProduct() {
        wishListDao?.removeFromWishList(product.id)
        val productTest = wishListDao?.getWishListItemWith(product.id)
        assertNull(productTest?.getOrAwaitValue())
    }

    @Test
    fun shouldInsertAll() {
        val anotherProduct = Product(2, "Test Product 2", "Test Description 2", oldPrice = "3.00", price = "4.89", stock = 3);
        val listOfNewProducts = listOf(product, anotherProduct)
        wishListDao?.insertWishListProducts(listOfNewProducts)

        val allNewlyAddedProducts = wishListDao?.getWishList()?.getOrAwaitValue()
        assertEquals(allNewlyAddedProducts?.size, listOfNewProducts.size )
    }

    @Test
    fun databaseIsAtomic() {
        val listOfNewProducts = listOf(product, product, product, product)
        wishListDao?.deleteAll()
        wishListDao?.insertWishListProducts(listOfNewProducts)

        val allNewlyAddedProducts = wishListDao?.getWishList()?.getOrAwaitValue()
        assertEquals(allNewlyAddedProducts?.size, 1 )
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