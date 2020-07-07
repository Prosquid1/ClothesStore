package com.oyelekeokiki

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BaseCartImplModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteApi: RemoteApi

    @Test
    fun testAddedCartItem() {
        runBlocking {
            doAnswer { null }
                .`when`(remoteApi).addProductToCart(1)
        }
    }
}