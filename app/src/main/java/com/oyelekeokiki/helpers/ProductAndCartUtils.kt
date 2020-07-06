package com.oyelekeokiki.helpers

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartToProductItem
import com.oyelekeokiki.model.Product

fun List<Product>.convertToCartProduct(cartItemIds: List<CartItem>): List<CartToProductItem> {
    val cartItemsGroupedByProduct = cartItemIds.groupBy { it.productId }.values

    val productIdToProductMap = this.map { Pair(it.id, it) }.toMap()
    val productIdToCartItemsMap =
        cartItemsGroupedByProduct.map { array -> Pair(array[0].productId, array.map { it.id }) }
            .toMap()

    return productIdToCartItemsMap.map {
        CartToProductItem(
            productIdToProductMap[it.key] as Product,
            productIdToCartItemsMap[it.key] as List<Int>
        )
    }
}

fun List<Product>.getProductsInIDsList(productIds: List<Int>): List<Product> {
    return productIds.flatMap { mappedId -> this.filter { mappedId == it.id } }
}

fun List<CartToProductItem>.getTotalValue(): String {
    return this.toList()
        .sumBy { ((it.product.price ?: "").toDouble().toInt() * it.cartItemIds.size) }.toString()
}

enum class ActionResponseType() {
    SUCCESS, ERROR;
}