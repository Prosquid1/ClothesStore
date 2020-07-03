package com.oyelekeokiki.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val remoteApi: RemoteApi) : ViewModel() {
    var products: MutableLiveData<List<Product>> = MutableLiveData()
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
                    filterProductsFromWishList(result.data)

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

    /**
     * Check which products are on wish list and filter this list to mark as added
     * Do this in adapter??
     * **/
    private fun filterProductsFromWishList(productsFromServer: List<Product>) {
        products.postValue(productsFromServer)
    }

    fun modifyWishListWithProduct(product: Product, isLiked: Boolean) {
        //TODO: Implement
        Log.e("Product","${product.name} is liked? ${isLiked}")
    }
}