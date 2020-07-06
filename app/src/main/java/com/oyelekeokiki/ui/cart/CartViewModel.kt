package com.oyelekeokiki.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.helpers.*
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartItemsToProduct
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.BaseCartImplModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val remoteApi: RemoteApi
) : BaseCartImplModel(remoteApi) {
    var cartItems: MutableLiveData<List<CartItemsToProduct>> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var totalValueText: MutableLiveData<String> = MutableLiveData()
    var isFetching: MutableLiveData<Boolean> = MutableLiveData()

    var cartItemDeletedSuccess: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()
    var cartItemDeletedFailed: MutableLiveData<Triple<CartItem, String, ActionResponseType>> =
        MutableLiveData()

    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            isFetching.postValue(true)
            try {
                val cartResult = remoteApi.getCart()
                if (cartResult is Success) {
                    queryCartItemsForProducts(cartResult.data)
                } else if (cartResult is Failure) {
                    isFetching.postValue(false)
                    errorMessage.postValue(cartResult.error?.localizedMessage)
                }

            } catch (e: Exception) {
                errorMessage.postValue(ExceptionUtil.getFetchExceptionMessage(e))
                isFetching.postValue(false);
            }
        }
    }

    //Supposed to be a server call with an array of product Ids
    private fun queryCartItemsForProducts(productsInCartIds: List<CartItem>) {
        viewModelScope.launch {
            try {
                val productsResult = remoteApi.getProducts()
                if (productsResult is Success) {
                    val serverProducts = productsResult.data
                    val cartToProductItems = serverProducts.convertToCartProduct(productsInCartIds)
                    cartItems.postValue(cartToProductItems)
                    totalValueText.postValue(cartToProductItems.getTotalValueString().formatPrice())
                }
                isFetching.postValue(false);
            } catch (e: Exception) {
                errorMessage.postValue(ExceptionUtil.getFetchExceptionMessage(e))
                isFetching.postValue(false);
            }
        }
    }

    fun deleteFromCart(cartItem: CartItem) {
        if (cartItem.id == null) {
            throw IllegalArgumentException("Delete from cart being called from an illegal reference")
        }

        viewModelScope.launch {
            try {
                val result = remoteApi.deleteProductFromCart(cartItem.id)
                if (result is Success) {
                    cartItemDeletedSuccess.postValue(
                        Triple(
                            cartItem,
                            "Deleted successfully!",
                            ActionResponseType.SUCCESS
                        )
                    )
                    fetchCartItems()
                }
            } catch (e: Exception) {
                cartItemDeletedFailed.postValue(
                    Triple(
                        cartItem,
                        ExceptionUtil.getFetchExceptionMessage(e),
                        ActionResponseType.ERROR
                    )
                )
            }
        }
    }

    override fun onAddToCartComplete(productId: Int) {
        fetchCartItems()
    }
}