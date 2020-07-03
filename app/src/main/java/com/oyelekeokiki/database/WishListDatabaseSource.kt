package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import com.oyelekeokiki.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishListDatabaseSource @Inject constructor(private val wishListDao: WishListDao) {
    suspend fun addToWishList(product: Product) = wishListDao.addToWishList(product)

    suspend fun removeFromWishList(productId: Int) = wishListDao.removeFromWishList(productId)

    fun getWishList(): LiveData<List<Product>> = wishListDao.getWishList()

    fun getWishListIds(): LiveData<List<Int>> = wishListDao.getWishListIds()

    fun getWishListCount(): LiveData<Int> = wishListDao.getWishListCount()

}
