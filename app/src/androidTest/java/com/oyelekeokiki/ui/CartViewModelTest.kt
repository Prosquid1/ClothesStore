package com.oyelekeokiki.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.oyelekeokiki.helpers.*
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartItemsToProduct
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.cart.CartViewModel
import com.oyelekeokiki.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var remoteApi: RemoteApi

    @Test
    fun getCart() {
        runBlocking {
            val expectedResult = MockObjectProvider.provideCartItems()
            doReturn(expectedResult)
                .`when`(remoteApi)
                .getCart()
            val viewModel = CartViewModel(remoteApi)
            verify(remoteApi).getCart()

            assert(viewModel.cartItems == expectedResult)
        }
    }

    fun getAllProducts() {
        runBlocking {
            val expectedResult = MockObjectProvider.provideProducts()
            doReturn(expectedResult)
                .`when`(remoteApi)
                .getProducts()
            val viewModel = CartViewModel(remoteApi)
            verify(remoteApi).getProducts()
            assert(viewModel.cartProducts == expectedResult)
        }
    }

    @Test
    fun testDeletedCartItem() {
        runBlocking {
            doAnswer { null }
                .`when`(remoteApi).deleteProductFromCart(1)
        }
    }
}