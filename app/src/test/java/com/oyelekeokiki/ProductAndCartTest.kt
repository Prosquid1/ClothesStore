package com.oyelekeokiki

import com.oyelekeokiki.helpers.MockObjectProvider
import com.oyelekeokiki.helpers.convertToCartItemsToProduct
import com.oyelekeokiki.helpers.getTotalValueString
import org.junit.Test

class ProductAndCartTest {
    @Test
    fun convertToCartProductTest() {
        val mockingCartToProductItem = MockObjectProvider.provideProducts()
            .convertToCartItemsToProduct(MockObjectProvider.provideCartItems())

        val expectedCartToProductItem = MockObjectProvider.provideCartItemsToProduct()

        val allProductsAreSame = mockingCartToProductItem.map { it.product }
            .containsAll(expectedCartToProductItem.map { it.product })

        val cartIDsAreSame = mockingCartToProductItem.map { it.cartItemIds }
            .containsAll(expectedCartToProductItem.map { it.cartItemIds })

        assert(allProductsAreSame && cartIDsAreSame)

    }

    @Test
    fun getTotalValueTest() {
        val cartItemsToProducts = MockObjectProvider.provideCartItemsToProduct()
        assert(cartItemsToProducts.getTotalValueString() == "10")

    }
}