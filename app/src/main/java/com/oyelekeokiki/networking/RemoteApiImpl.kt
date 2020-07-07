package com.oyelekeokiki.networking

import com.oyelekeokiki.model.*
import com.oyelekeokiki.model.response.AddToCartResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Holds decoupled logic for all the API calls.
 */

class RemoteApiImpl @Inject constructor(
    private val apiService: RemoteApiService
) : RemoteApi {

    override suspend fun getProducts(): List<Product> {
        val data = apiService.getProducts()
        if (data.isNotEmpty()) {
            return data
        } else {
            throw NullPointerException("No products available")
        }
    }

    override suspend fun getCart(): List<CartItem> {
        val data = apiService.getCart()
        if (data.isNotEmpty()) {
            return data
        } else {
            throw NullPointerException("No products available")
        }
    }

    override suspend fun deleteProductFromCart(productId: Int): Response<Unit> {
        return apiService.deleteCartItem(productId)
    }

    override suspend fun addProductToCart(productId: Int): AddToCartResponse {
        return apiService.addCartItem(productId)
    }

}