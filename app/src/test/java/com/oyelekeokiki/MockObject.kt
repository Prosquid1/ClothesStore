package com.oyelekeokiki

import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartToProductItem
import com.oyelekeokiki.model.Product

object MockObject {

    const val LOW_STOCK_INT = 2
    const val MEDIUM_STOCK_INT = 5

    fun provideCartItems(): List<CartItem> {
        return arrayListOf(
            CartItem(1, 1),
            CartItem(2, 2),
            CartItem(3, 3),
            CartItem(4, 4)
        )
    }

    fun provideCartToProductItems(): List<CartToProductItem> {
        return arrayListOf(
            CartToProductItem(arrayListOf(1), provideProducts()[0]),
            CartToProductItem(arrayListOf(2,3), provideProducts()[1]),
            CartToProductItem(arrayListOf(4,5,6), provideProducts()[2]),
            CartToProductItem(arrayListOf(7,8,9,10), provideProducts()[3])
        )
    }

    fun provideProducts(): List<Product> {
        return arrayListOf(
            Product(
                1,
                "Test Product 1",
                "Test Description 1",
                oldPrice = "1.00",
                price = "1.11",
                stock = 1
            ), Product(
                2,
                "Test Product 2",
                "Test Description 2",
                oldPrice = "2.00",
                price = "2.22",
                stock = 2
            ), Product(
                3,
                "Test Product 3",
                "Test Description 3",
                oldPrice = "3.00",
                price = "3.33",
                stock = 3
            ), Product(
                4,
                "Test Product 4",
                "Test Description 4",
                oldPrice = "3.00",
                price = "4.44",
                stock = 4
            )
        )
    }
}