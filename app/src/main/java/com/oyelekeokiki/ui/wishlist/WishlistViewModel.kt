package com.oyelekeokiki.ui.wishlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.model.Product
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishlistViewModel @Inject constructor(private val wishListDatabaseSource: WishListDatabaseSource ) : ViewModel() {
    var wishlist: MutableLiveData<List<Product>> =  MutableLiveData()
    var wishListProductIds: MutableLiveData<List<Int>> = MutableLiveData()
    var wishListIsEmpty: MutableLiveData<Boolean> = MutableLiveData()

    init {
        fetchWishList()
    }

    fun updateWishListWithProduct(product: Product, isLiked: Boolean) {
        //TODO: Implement
        viewModelScope.launch {
            if (isLiked) {
                wishListDatabaseSource.addToWishList(product)
            } else {
                wishListDatabaseSource.removeFromWishList(product.id)
            }
        }
    }

    private fun fetchWishList() {
        viewModelScope.launch {
            try {
                val dbWishList = wishListDatabaseSource.getWishList()
                wishlist.postValue(dbWishList)
                wishListProductIds.postValue(dbWishList.map { it.id })
                wishListIsEmpty.postValue(dbWishList.isEmpty())

            } catch (e: Exception) {
                wishListProductIds.postValue(arrayListOf())
            }
        }
    }
}