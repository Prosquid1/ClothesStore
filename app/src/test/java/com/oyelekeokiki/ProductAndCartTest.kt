package com.oyelekeokiki

import com.oyelekeokiki.utils.convertToCartProduct
import com.oyelekeokiki.utils.getTotalValueString
import org.junit.Test

class ProductAndCartTest {
    @Test
    fun convertToCartProductTest() {
        val mockingCartToProductItem = MockObject.provideProducts()
            .convertToCartProduct(MockObject.provideCartItems())

        val expectedCartToProductItem = MockObject.provideCartItemsToProduct()

        val allProductsAreSame = mockingCartToProductItem.map { it.product }
            .containsAll(expectedCartToProductItem.map { it.product })

        val cartIDsAreSame = mockingCartToProductItem.map { it.cartItemIds }
            .containsAll(expectedCartToProductItem.map { it.cartItemIds })

        assert(allProductsAreSame && cartIDsAreSame)

    }

    @Test
    fun getTotalValueTest() {
        val cartItemsToProducts = MockObject.provideCartItemsToProduct()
        assert(cartItemsToProducts.getTotalValueString() == "10")

    }
}