package com.oyelekeokiki.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.oyelekeokiki.database.AppDataBase
import com.oyelekeokiki.database.WishListDao
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.db.getOrAwaitValue
import com.oyelekeokiki.helpers.MockObjectProvider
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import java.io.IOException


/**
 * Ideally, Database tests should be executed on an Android device.
 * See [testing documentation](https://developer.android.com/training/data-storage/room/testing-db).
 */
@RunWith(AndroidJUnit4::class)
class WishlistModelTest {

    private var wishListDao: WishListDao? = null
    private var appDataBase: AppDataBase? = null

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var wishlistObserver: Observer<List<Product>>

    @Mock
    private lateinit var databaseSource: WishListDatabaseSource

    @Mock
    private lateinit var remoteApi: RemoteApi

    private val product = MockObjectProvider.provideProducts()[0]

    @Before
    fun setUp() {
        databaseSource = WishListDatabaseSource(Mockito.mock(WishListDao::class.java))
    }


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
        appDataBase?.close()
    }

    @Test
    fun onAddToCartComplete() = runBlocking {
        wishListDao?.addToWishList(MockObjectProvider.provideProducts()[0])
        val currentCartCount: Int = wishListDao?.getWishListCount()?.getOrAwaitValue() ?: 0
        wishListDao?.addToWishList(MockObjectProvider.provideProducts()[1])
        val expectedCartCount = wishListDao?.getWishListCount()?.getOrAwaitValue()
        assertEquals((currentCartCount + 1), expectedCartCount)
    }

    @Test
    fun shouldInsertProductsFromServer() = runBlocking {
        wishListDao?.addToWishList(product)
        val expectedProduct = wishListDao?.getWishListItemWith(product.id)
        assertEquals(product.id, expectedProduct?.id)
    }

}
