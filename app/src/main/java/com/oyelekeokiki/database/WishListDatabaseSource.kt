package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oyelekeokiki.model.Product
import javax.inject.Inject

class WishListDatabaseSource @Inject constructor(private val wishListDao: WishListDao) {
    fun addToWishList(product: Product) = wishListDao.addToWishList(product)

    fun insertWishListProducts(products: List<Product>) = wishListDao.insertWishListProducts(products)

    fun removeFromWishList(productId: Int) = wishListDao.removeFromWishList(productId)

    fun getWishList(): LiveData<List<Product>> = wishListDao.getWishList()

    fun getWishListIds(): LiveData<List<Int>> = wishListDao.getWishListIds()

    fun getWishListItemWith(productId: Int): LiveData<Product> = wishListDao.getWishListItemWith(productId)

    fun deleteAll() = wishListDao.deleteAll()

    fun getProductsCount(): Int = wishListDao.getProductsCount()

}
