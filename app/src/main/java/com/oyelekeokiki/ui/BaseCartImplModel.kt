package com.oyelekeokiki.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.database.WishListDatabaseSource
import com.oyelekeokiki.helpers.ActionResponseType
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * All views in this project have the same add and delete from cart model and observable variables
 * **/

open class BaseCartImplModel @Inject constructor(
    private val remoteApi: RemoteApi,
    private val wishListDatabaseSource: WishListDatabaseSource,
    application: Application
) : AndroidViewModel(application) {
    var addToCartSuccess: MutableLiveData<Triple<String, String, ActionResponseType>> =
        MutableLiveData()
    var addToCartFailed: MutableLiveData<Triple<String, String, ActionResponseType>> =
        MutableLiveData()

    fun addToCart(productId: String) {
        viewModelScope.launch {
            try {
                val result = remoteApi.addProductToCart(productId)
                if (result is Success) {
                    addToCartSuccess.postValue(
                        Triple(
                            productId,
                            result.data.message,
                            ActionResponseType.SUCCESS
                        )
                    )
                    updateProductCountInWishList(productId, -1)
                } else if (result is Failure) {
                    addToCartFailed.postValue(
                        Triple(
                            productId,
                            result.error?.message ?: "An error occurred!",
                            ActionResponseType.ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                addToCartFailed.postValue(
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
        viewModelScope.launch {
            try {
                val result = remoteApi.deleteProductFromCart(productId)
                if (result is Success) {
                    addToCartSuccess.postValue(
                        Triple(
                            productId,
                            result.data.message,
                            ActionResponseType.SUCCESS
                        )
                    )
                    updateProductCountInWishList(productId, 1)
                } else if (result is Failure) {
                    addToCartFailed.postValue(
                        Triple(
                            productId,
                            result.error?.message ?: "An error occurred!",
                            ActionResponseType.ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                addToCartFailed.postValue(
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