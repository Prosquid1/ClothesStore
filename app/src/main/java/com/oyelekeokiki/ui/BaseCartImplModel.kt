package com.oyelekeokiki.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.ActionResponseType
import com.oyelekeokiki.helpers.NO_INTERNET_CONNECTION
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.NetworkStatusChecker
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * All views in this project have the same add and delete from cart model and observable variables
 * **/

open class BaseCartImplModel constructor(
    private val remoteApi: RemoteApi,
    private val wishListDatabaseSource: WishListDatabaseSource,
    private val networkStatusChecker: NetworkStatusChecker,
    application: Application
) : AndroidViewModel(application) {
    var cartUpdateSuccess: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()
    var cartUpdateFailed: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()

    fun showCartInternetErrorWithRetry(cartItem: CartItem) {
        cartUpdateFailed.postValue(
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
                    cartUpdateSuccess.postValue(
                        Triple(
                            cartItem,
                            result.data.message,
                            ActionResponseType.SUCCESS
                        )
                    )
                    updateProductCountInWishList(cartItem.productId, -1)
                } else if (result is Failure) {
                    cartUpdateFailed.postValue(
                        Triple(
                            cartItem,
                            result.error?.message ?: "An error occurred!",
                            ActionResponseType.ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                cartUpdateFailed.postValue(
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
     * This is an instantaneous function to reflect an item has been updated
     * In the rare case the WishList items cannot be updated from server and has to rely on offline data
     * */

    fun updateProductCountInWishList(productId: Int, count: Int) {
        viewModelScope.launch {
            wishListDatabaseSource.updateProductStockCount(productId, count)
        }
    }
}