package com.oyelekeokiki.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val remoteApi: RemoteApi, private val wishListDatabaseSource: WishListDatabaseSource ) : ViewModel() {
    var products: MutableLiveData<List<Product>> = MutableLiveData()
    var wishListProductIds: LiveData<List<Int>> = wishListDatabaseSource.getWishListIds()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var isFetching: MutableLiveData<Boolean> = MutableLiveData()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            isFetching.postValue(true);
            try {
                val result = remoteApi.getProducts()
                if (result is Success) {
                    products.postValue(result.data)

                } else if (result is Failure) {
                    errorMessage.postValue(result.error?.localizedMessage)
                }
                isFetching.postValue(false);
            } catch (e: Exception) {
                errorMessage.postValue(e.localizedMessage)
                isFetching.postValue(false);
            }
        }
    }

    private fun observeWishList() {

    }
    /**
     * Check which products are on wish list and filter this list to mark as added
     * Do this in adapter??
     * **/
    private fun filterProductsFromWishList(productsFromServer: List<Product>) {

    }

    fun updateWishListWithProduct(product: Product, isLiked: Boolean) {
        //TODO: Implement
        Log.e("Product","${product.name} is liked? ${isLiked}")
        if (isLiked) {
            wishListDatabaseSource.addToWishList(product)
        } else {
            wishListDatabaseSource.removeFromWishList(product.id)
        }
    }
}