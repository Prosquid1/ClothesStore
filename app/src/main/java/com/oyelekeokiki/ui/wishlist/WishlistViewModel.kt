package com.oyelekeokiki.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.model.Product
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishlistViewModel @Inject constructor(private val wishListDatabaseSource: WishListDatabaseSource ) : ViewModel() {
    var wishlist: LiveData<List<Product>> =  wishListDatabaseSource.getWishList()
    var wishListProductIds: LiveData<List<Int>> = wishListDatabaseSource.getWishListIds()

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
}