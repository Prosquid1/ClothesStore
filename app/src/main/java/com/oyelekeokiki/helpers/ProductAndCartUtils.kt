package com.oyelekeokiki.helpers

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartItemsToProduct
import com.oyelekeokiki.model.Product

/**
 * Since [RemoteApi][getCart] returns a [CartItem], but the viewHolder requires a [Product],
 * this method filters a list of cartItems present in [RemoteApi][getProducts] to [CartItemsToProduct]
 */

fun List<Product>.convertToCartItemsToProduct(cartItemIds: List<CartItem>): List<CartItemsToProduct> {
    val cartItemsGroupedByProduct = cartItemIds.groupBy { it.productId }
    // List of product IDs and all their cart ID occurences
    val productIdToProductMap = this.map { Pair(it.id, it) }.toMap()

    return cartItemsGroupedByProduct.map { CartItemsToProduct(
        it.value.map { cartItem -> cartItem.id ?: -1  }, //CartItem gotten from server cannot be null
        productIdToProductMap[it.key] as Product
    ) }
}

fun List<Product>.getProductsInIDsList(productIds: List<Int>): List<Product> {
    return productIds.flatMap { mappedId -> this.filter { mappedId == it.id } }
}

fun List<CartItemsToProduct>.getFormattedCartTotalPrice(): String {
    return this.sumBy { ((it.product.price ?: "").toDouble().toInt() * it.cartItemIds.size) }.toString().formatPrice()
}

enum class ActionResponseType {
    SUCCESS, ERROR;
}