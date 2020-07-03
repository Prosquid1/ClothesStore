package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import com.oyelekeokiki.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishListDatabaseSource @Inject constructor(private val wishListDao: WishListDao) {
    suspend fun addToWishList(product: Product) = wishListDao.addToWishList(product)

    suspend fun removeFromWishList(productId: Int) = wishListDao.removeFromWishList(productId)

    fun getWishList(): Flow<List<Product>> = wishListDao.getWishList()

    suspend fun getWishListIds(): List<Int> = wishListDao.getWishListIds()

}
