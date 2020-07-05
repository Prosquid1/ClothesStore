package com.oyelekeokiki.networking

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.response.AddToCartResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Holds the API calls for the app.
 */
interface RemoteApiService {

  @GET("products")
  suspend fun getProducts(): List<Product>

  @GET("cart")
  suspend fun getCart(): List<CartItem>

  @POST("cart")
  suspend fun addCartItem(@Query("productId") productId: String): AddToCartResponse

  @DELETE("cart")
  suspend fun deleteCartItem(@Query("id") productId: String): Response<Unit>

}