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

  override suspend fun getProducts(): Result<List<Product>> = try {
    val data = apiService.getProducts()
    if (data.isNotEmpty()) {
      Success(data)
    } else {
      Failure(NullPointerException("No products available"))
    }
  } catch (error: Throwable) {
    Failure(error)
  }

  override suspend fun getCart(): Result<List<CartItem>> = try {
    val data = apiService.getCart()
    Success(data)
  } catch (error: Throwable) {
    Failure(error)
  }

  override suspend fun deleteProductFromCart(productId: String): Result<Response<Unit>> = try {
    val data = apiService.deleteCartItem(productId)
    Success(data)
  } catch (error: Throwable) {
    Failure(error)
  }

  override suspend fun addProductToCart(productId: String): Result<AddToCartResponse> = try {
    val product = apiService.addCartItem(productId)
    Success(product)
  } catch (error: Throwable) {
    Failure(error)
  }

}