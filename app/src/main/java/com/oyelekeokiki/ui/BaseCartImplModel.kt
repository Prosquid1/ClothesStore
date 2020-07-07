package com.oyelekeokiki.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.helpers.ActionResponseType
import com.oyelekeokiki.helpers.ExceptionUtil
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch

/**
 * All views in this project call [addToCart]
 * [WishListFragment] and [HomeFragment] can observe [cartItemAddedSuccess] and [cartItemAddedFailed]
 * Hence the need for a base class
 **/

open class BaseCartImplModel constructor(
    private val remoteApi: RemoteApi
) : ViewModel() {
    var cartItemAddedSuccess: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()
    var cartItemAddedFailed: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            try {
                val result = remoteApi.addProductToCart(cartItem.productId)
                cartItemAddedSuccess.postValue(
                    Triple(
                        cartItem,
                        result.message,
                        ActionResponseType.SUCCESS
                    )
                )
                onAddToCartComplete(cartItem.productId)
            } catch (e: Exception) {
                cartItemAddedFailed.postValue(
                    Triple(
                        cartItem,
                        ExceptionUtil.getFetchExceptionMessage(e),
                        ActionResponseType.ERROR
                    )
                )
            }
        }
    }

    /**
     *  To be overriden by [HomeFragment] and [CartFragment]
     *  There is a need to re-query because [AddToCartResponse] returns only a string,
     *  and the view needs to be updated with live data
     **/
    open fun onAddToCartComplete(productId: Int) {}

}