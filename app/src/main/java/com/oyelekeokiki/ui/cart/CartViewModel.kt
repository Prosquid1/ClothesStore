package com.oyelekeokiki.ui.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.NO_INTERNET_CONNECTION
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.NetworkStatusChecker
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.BaseCartImplModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val remoteApi: RemoteApi,
    private val wishListDatabaseSource: WishListDatabaseSource,
    private val networkStatusChecker: NetworkStatusChecker,
    application: Application
) : BaseCartImplModel(remoteApi, wishListDatabaseSource, networkStatusChecker, application) {
    var cartItems: MutableLiveData<List<Product>> = MutableLiveData()
    var wishListProductIds: LiveData<List<Int>> = wishListDatabaseSource.getWishListIds()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var isFetching: MutableLiveData<Boolean> = MutableLiveData()

    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        if (!networkStatusChecker.hasInternetConnection()) {
            isFetching.postValue(false);
            errorMessage.postValue(NO_INTERNET_CONNECTION)
            return
        }

        viewModelScope.launch {
            isFetching.postValue(true);
            try {
                val result = remoteApi.getProducts()
                if (result is Success) {
                    cartItems.postValue(result.data)

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
}