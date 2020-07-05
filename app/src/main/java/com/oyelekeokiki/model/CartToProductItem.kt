package com.oyelekeokiki.model

/**
 * Represents a cart to pr from the API.
 */

data class CartToProductItem(
  val cartId: Int,
  val product: Product,
  val count: Int
)