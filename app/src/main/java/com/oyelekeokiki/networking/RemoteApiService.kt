package com.oyelekeokiki.networking

import com.oyelekeokiki.model.response.AddToCartResponse
import com.oyelekeokiki.model.response.DeleteFromCartResponse
import com.oyelekeokiki.model.response.GetCartResponse
import com.oyelekeokiki.model.response.GetProductsResponse
import retrofit2.http.*

/**
 * Holds the API calls for the app.
 */
interface RemoteApiService {

  @GET("/products")
  suspend fun getProducts(): GetProductsResponse

  @GET("/cart")
  suspend fun getCart(): GetCartResponse

  @POST("/cart")
  suspend fun addCartItem(@Query("productId") productId: String): AddToCartResponse

  @DELETE("/cart")
  suspend fun deleteCartItem(@Query("id") productId: String): DeleteFromCartResponse

}