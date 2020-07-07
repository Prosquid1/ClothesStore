package com.oyelekeokiki.ui.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.getProductsInIDsList
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.BaseCartImplModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishlistViewModel @Inject constructor(
    private val remoteApi: RemoteApi,
    private val wishListDatabaseSource: WishListDatabaseSource
) : BaseCartImplModel(remoteApi) {
    var wishlist: LiveData<List<Product>> = wishListDatabaseSource.getWishList()
    var wishListProductIds: LiveData<List<Int>> = wishListDatabaseSource.getWishListIds()

    init {
        updateWishListProductsFromServer()
    }

    override fun onAddToCartComplete(productId: Int) {
        viewModelScope.launch {
            wishListDatabaseSource.updateProductStockCount(productId, -1)
        }
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
                val newestProductsData = result.getProductsInIDsList(wishListProductIds)
                wishListDatabaseSource.addToWishList(newestProductsData)
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