package com.oyelekeokiki.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.helpers.*
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartItemsToProduct
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
                queryCartItemsForProducts(cartResult)
            } catch (e: Exception) {
                errorMessage.postValue(ExceptionUtil.getFetchExceptionMessage(e))
                isFetching.postValue(false);
            }
        }
    }

    //Supposed to be a server call with an array of product Ids
    private suspend fun queryCartItemsForProducts(productsInCartIds: List<CartItem>) {
        val serverProducts = remoteApi.getProducts()
        val cartToProductItems = serverProducts.convertToCartProduct(productsInCartIds)
        cartItems.postValue(cartToProductItems)
        totalValueText.postValue(cartToProductItems.getTotalValueString().formatPrice())
        isFetching.postValue(false);
    }

    fun deleteFromCart(cartItem: CartItem) {
        if (cartItem.id == null) {
            throw IllegalArgumentException("Delete from cart being called from an illegal reference")
        }

        viewModelScope.launch {
            try {
                remoteApi.deleteProductFromCart(cartItem.id)
                cartItemDeletedSuccess.postValue(
                    Triple(
                        cartItem,
                        "Deleted successfully!",
                        ActionResponseType.SUCCESS
                    )
                )
                fetchCartItems()

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