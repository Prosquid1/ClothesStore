package com.oyelekeokiki.helpers

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Product

const val AVAILABLE_ITEM_ALPHA = 1f
const val DELETED_CART_SUCCESS = "Deleted successfully!"
const val ILLEGAL_CART_DELETION = "Not allowed!"
const val MINIMUM_STOCK_THRESHOLD = 3
const val NO_INTERNET_CONNECTION = "No Internet connection!"
const val NO_STOCK_INT = 0
const val OUT_OF_STOCK_TEXT = "(Out of stock)"
const val SOLD_OUT_ITEM_ALPHA = 0.34f
const val UNKNOWN_ERROR = "An unknown error occurred!"


typealias OnCartModified = (cartItem: CartItem) -> Unit
typealias OnWishListModified = (product: Product, isLiked: Boolean) -> Unit
