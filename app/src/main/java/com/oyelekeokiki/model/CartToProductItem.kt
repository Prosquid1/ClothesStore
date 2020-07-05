package com.oyelekeokiki.model

/**
 * Represents a cart to product mapping fulfilled by @see Utils.kt
 */

data class CartToProductItem(
  val product: Product,
  val cartItemIds: List<Int>
)