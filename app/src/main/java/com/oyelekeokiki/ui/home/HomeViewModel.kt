package com.oyelekeokiki.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.ExceptionUtil
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.BaseCartImplModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val remoteApi: RemoteApi,
    private val wishListDatabaseSource: WishListDatabaseSource
) : BaseCartImplModel(remoteApi) {
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
                products.postValue(result)
                isFetching.postValue(false)
            } catch (e: Exception) {
                errorMessage.postValue(ExceptionUtil.getFetchExceptionMessage(e))
                isFetching.postValue(false)
            }
        }
    }

    fun updateWishListWithProduct(product: Product, isLiked: Boolean) {
        viewModelScope.launch {
            if (isLiked) {
                wishListDatabaseSource.addToWishList(product)
            } else {
                wishListDatabaseSource.removeFromWishList(product.id)
            }
        }
    }

    override fun onAddToCartComplete(productId: Int) {
        fetchProducts()
    }
}