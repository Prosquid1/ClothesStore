package com.oyelekeokiki.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.utils.ActionResponseType
import com.oyelekeokiki.utils.NO_INTERNET_CONNECTION
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.NetworkStatusChecker
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch

/**
 * All views in this project have the same add and delete from cart model and observable variables
 * **/

open class BaseCartImplModel constructor(
    private val remoteApi: RemoteApi,
    private val networkStatusChecker: NetworkStatusChecker,
    application: Application
) : AndroidViewModel(application) {
    var cartItemAddedSuccess: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()
    var cartItemAddedFailed: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()

    fun showCartInternetErrorWithRetry(cartItem: CartItem) {
        cartItemAddedFailed.postValue(
            Triple(
                cartItem,
                NO_INTERNET_CONNECTION,
                ActionResponseType.ERROR
            )
        )
    }

    fun addToCart(cartItem: CartItem) {
        if (!networkStatusChecker.hasInternetConnection()) {
            showCartInternetErrorWithRetry(cartItem)
            return
        }

        viewModelScope.launch {
            try {
                val result = remoteApi.addProductToCart(cartItem.productId)
                if (result is Success) {
                    cartItemAddedSuccess.postValue(
                        Triple(
                            cartItem,
                            result.data.message,
                            ActionResponseType.SUCCESS
                        )
                    )
                    onAddToCartComplete(cartItem.productId)

                } else if (result is Failure) {
                    cartItemAddedFailed.postValue(
                        Triple(
                            cartItem,
                            result.error?.message ?: "An error occurred!",
                            ActionResponseType.ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                cartItemAddedFailed.postValue(
                    Triple(
                        cartItem,
                        e.localizedMessage,
                        ActionResponseType.ERROR
                    )
                )
            }
        }
    }

    /**
     *To be overriden
     *This is not a good approach, I only implemented because products cannot be queried by ID (on API) or stored on the device
     * **/
    open fun onAddToCartComplete(productId: Int) {}

    /**
     * This is an instantaneous function to reflect an item has been updated
     * In the rare case the WishList items cannot be updated from server and has to rely on offline data
     * */

    fun updateProductCountInWishList(productId: Int, count: Int) {

    }
}