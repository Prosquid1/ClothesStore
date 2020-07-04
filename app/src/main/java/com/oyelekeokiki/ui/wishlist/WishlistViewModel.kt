package com.oyelekeokiki.ui.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.getProductsInIDsList
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishlistViewModel @Inject constructor(private val remoteApi: RemoteApi,
                                            private val wishListDatabaseSource: WishListDatabaseSource ) : ViewModel() {
    var wishlist: LiveData<List<Product>> =  wishListDatabaseSource.getWishList()
    var wishListProductIds: LiveData<List<Int>> = wishListDatabaseSource.getWishListIds()

    init {
        updateWishListProductsFromServer()
    }

    /**
     * Merge items from server into items with database
     * This is because product details might have been changed by admin or another client
     * **/
    private fun updateWishListProductsFromServer() {
        viewModelScope.launch {
            try {
                val result = remoteApi.getProducts()
                val wishListProductIds = wishListDatabaseSource.getWishListIdsSync()
                if (result is Success) {
                    val newestProductsData = result.data.getProductsInIDsList(wishListProductIds)
                    wishListDatabaseSource.addToWishList(newestProductsData)
                } else if (result is Failure) {
                    Log.e("Fetching products Fail", result.error?.message ?: "N/A")
                }
            } catch (e: Exception) {
                Log.e("Fetching products Exc", e.localizedMessage ?: "N/A")
            }
        }
    }

    fun updateWishListWithProductChanged(product: Product, isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                wishListDatabaseSource.addToWishList(product)
            } else {
                wishListDatabaseSource.removeFromWishList(product.id)
            }
        }
    }
}