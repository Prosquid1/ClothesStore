package com.oyelekeokiki.networking

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Result
import com.oyelekeokiki.model.response.AddToCartResponse
import retrofit2.Response

interface RemoteApi {
  suspend fun getProducts(): Result<List<Product>>

  suspend fun getCart(): Result<List<CartItem>>

  suspend fun addProductToCart(productId: String): Result<AddToCartResponse>

  suspend fun deleteProductFromCart(productId: String): Result<Response<Unit>>
}
