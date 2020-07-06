package com.oyelekeokiki.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a product from the API.
 */

@Entity
data class Product(
  @PrimaryKey val id: Int,
  val name: String,
  val category: String,
  val price: String?,
  val oldPrice: String?,
  val stock: Int
)