package com.oyelekeokiki

import com.oyelekeokiki.model.Product

object MockObject {

    fun provideTestString(): String {
        return "This is a test string"
    }

    fun provideSingleTestProduct(): Product {
        return provideTestProducts()[0]
    }

    fun provideTestProducts(): List<Product> {
        return arrayListOf(Product(
            1,
            "Test Product",
            "Test Description",
            oldPrice = "2.00",
            price = "1.89",
            stock = 2
        ), Product(
            2,
            "Test Product 2",
            "Test Description 2",
            oldPrice = "3.00",
            price = "4.89",
            stock = 3
        ))
    }
}