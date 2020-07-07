package com.oyelekeokiki

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.oyelekeokiki.database.AppDataBase
import com.oyelekeokiki.database.WishListDao
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.MockObjectProvider
import com.oyelekeokiki.helpers.NO_INTERNET_CONNECTION
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private var appDataBase: AppDataBase? = null

    @Mock
    private lateinit var remoteApi: RemoteApi
    @Mock
    private lateinit var databaseSource: WishListDatabaseSource

    @Mock
    private lateinit var apiProductsObserver: Observer<List<Product>>

    @Mock
    private lateinit var errorMessageObserver: Observer<String>

    @Before
    fun setUp() {
        databaseSource = WishListDatabaseSource(mock(WishListDao::class.java))
    }

    @Test
    fun serverReturnsDataOnSuccess() {
        runBlocking {
            val expectedResult = MockObjectProvider.provideProducts()
            doReturn(expectedResult)
                .`when`(remoteApi)
                .getProducts()
            val viewModel = HomeViewModel(remoteApi, databaseSource)
            viewModel.products.observeForever(apiProductsObserver)
            verify(remoteApi).getProducts()

            verify(apiProductsObserver).onChanged(
                expectedResult
            )
            viewModel.products.removeObserver(apiProductsObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        runBlocking {
            doThrow(RuntimeException(NO_INTERNET_CONNECTION))
                .`when`(remoteApi)
                .getProducts()
            val viewModel = HomeViewModel(remoteApi, databaseSource)
            viewModel.errorMessage.observeForever(errorMessageObserver)
            verify(remoteApi).getProducts()
            verify(errorMessageObserver).onChanged(
                NO_INTERNET_CONNECTION
            )
            viewModel.errorMessage.removeObserver(errorMessageObserver)
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        appDataBase?.close()
    }
}