package com.oyelekeokiki.model

/**
 * Represents a cart to product mapping fulfilled by @see Utils.kt
 */

data class CartToProductItem(
  val cartItemIds: List<Int>,
  val product: Product
)