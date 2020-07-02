package com.oyelekeokiki.networking

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Result
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.model.response.AddToCartResponse
import com.oyelekeokiki.model.response.DeleteFromCartResponse
import javax.inject.Inject

/**
 * Holds decoupled logic for all the API calls.
 */

class RemoteApiImpl @Inject constructor(
  private val apiService: RemoteApiService
) : RemoteApi {

  override suspend fun getProducts(): Result<List<Product>> = try {
    val data = apiService.getProducts().products

    Success(data)
  } catch (error: Throwable) {
    Failure(error)
  }

  override suspend fun getCart(): Result<List<CartItem>> = try {
    val data = apiService.getCart().cartItems

    Success(data)
  } catch (error: Throwable) {
    Failure(error)
  }

  override suspend fun deleteProductFromCart(productId: String): Result<DeleteFromCartResponse> = try {
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