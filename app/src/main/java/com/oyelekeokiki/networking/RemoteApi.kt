package com.oyelekeokiki.networking

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.response.AddToCartResponse
import retrofit2.Response

interface RemoteApi {
    suspend fun getProducts(): List<Product>

    suspend fun getCart(): List<CartItem>

    suspend fun addProductToCart(productId: Int): AddToCartResponse

    suspend fun deleteProductFromCart(productId: Int): Response<Unit>
}
