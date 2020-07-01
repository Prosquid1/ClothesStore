package com.oyelekeokiki.model

/**
 * Represents a product from the API.
 */

data class Product(
  val id: Int,
  val name: String,
  val category: String,
  val price: String,
  val oldPrice: String,
  val stock: Int
)