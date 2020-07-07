package com.oyelekeokiki.model

/**
 * Represents a cart to product mapping
 * The mapping is fulfilled by [convertToCartItemsToProduct] in [ProductAndCartUtils]
 */

data class CartItemsToProduct(
    val cartItemIds: List<Int>,
    val product: Product
)