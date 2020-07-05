package com.oyelekeokiki.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.ActionResponseType
import com.oyelekeokiki.helpers.NO_INTERNET_CONNECTION
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
    private val wishListDatabaseSource: WishListDatabaseSource,
    private val networkStatusChecker: NetworkStatusChecker,
    application: Application
) : AndroidViewModel(application) {
    var cartUpdateSuccess: MutableLiveData<Triple<String, String, ActionResponseType>> =
        MutableLiveData()
    var cartUpdateFailed: MutableLiveData<Triple<String, String, ActionResponseType>> =
        MutableLiveData()

    private fun showCartInternetErrorWithRetry(retryingProductId: String) {
        cartUpdateFailed.postValue(
            Triple(
                retryingProductId,
                NO_INTERNET_CONNECTION,
                ActionResponseType.ERROR
            )
        )
    }

    fun addToCart(productId: String) {
        if (!networkStatusChecker.hasInternetConnection()) {
            showCartInternetErrorWithRetry(productId)
            return
        }

        viewModelScope.launch {
            try {
                val result = remoteApi.addProductToCart(productId)
                if (result is Success) {
                    cartUpdateSuccess.postValue(
                        Triple(
                            productId,
                            result.data.message,
                            ActionResponseType.SUCCESS
                        )
                    )
                    updateProductCountInWishList(productId, -1)
                } else if (result is Failure) {
                    cartUpdateFailed.postValue(
                        Triple(
                            productId,
                            result.error?.message ?: "An error occurred!",
                            ActionResponseType.ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                cartUpdateFailed.postValue(
                    Triple(
                        productId,
                        e.localizedMessage,
                        ActionResponseType.ERROR
                    )
                )
            }
        }
    }

    fun deleteFromCart(productId: String) {
        if (!networkStatusChecker.hasInternetConnection()) {
            showCartInternetErrorWithRetry(productId)
            return
        }
        viewModelScope.launch {
            try {
                val result = remoteApi.deleteProductFromCart(productId)
                if (result is Success) {
                    cartUpdateSuccess.postValue(
                        Triple(
                            productId,
                            result.data.message,
                            ActionResponseType.SUCCESS
                        )
                    )
                    updateProductCountInWishList(productId, 1)
                } else if (result is Failure) {
                    cartUpdateFailed.postValue(
                        Triple(
                            productId,
                            result.error?.message ?: "An error occurred!",
                            ActionResponseType.ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                cartUpdateFailed.postValue(
                    Triple(
                        productId,
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

    private fun updateProductCountInWishList(productId: String, count: Int) {
        viewModelScope.launch {
            wishListDatabaseSource.updateProductStockCount(productId.toInt(), count)
        }
    }
}