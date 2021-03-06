package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import com.oyelekeokiki.model.Product
import javax.inject.Inject

open class WishListDatabaseSource @Inject constructor(private val wishListDao: WishListDao) {

    suspend fun addToWishList(products: List<Product>) = wishListDao.insertWishListProducts(products)

    suspend fun addToWishList(product: Product) = wishListDao.addToWishList(product)

    suspend fun decrementProductStockCount(productId: Int) = wishListDao.decrementProductStockCount(productId)

    suspend fun getWishListIdsSync(): List<Int> = wishListDao.getWishListIds()

    suspend fun removeFromWishList(productId: Int) = wishListDao.removeFromWishList(productId)


    fun getWishList(): LiveData<List<Product>> = wishListDao.getWishList()

    fun getWishListIds(): LiveData<List<Int>> = wishListDao.getLiveWishListIds()

}
