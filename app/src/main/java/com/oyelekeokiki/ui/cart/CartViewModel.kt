package com.oyelekeokiki.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.helpers.*
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartItemsToProduct
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.networking.RemoteApi
import com.oyelekeokiki.ui.BaseCartImplModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val remoteApi: RemoteApi
) : BaseCartImplModel(remoteApi) {

    // For testing, due to the API's constraints
    var cartItems: List<CartItem> = arrayListOf()
    var cartProducts: List<Product> = arrayListOf()

    var cartItemsToProducts: MutableLiveData<List<CartItemsToProduct>> = MutableLiveData()
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
                cartItems = remoteApi.getCart()
                getAllProducts()
            } catch (e: Exception) {
                errorMessage.postValue(ExceptionUtil.getFetchExceptionMessage(e))
                isFetching.postValue(false);
            }
        }
    }

    suspend fun getAllProducts() {
        cartProducts = remoteApi.getProducts()
        queryCartItemsForProducts(cartItems)
    }

    //Supposed to be a server call with an array of product Ids
     private fun queryCartItemsForProducts(productsInCartIds: List<CartItem>) {
        val convertedCartItemsToProducts = cartProducts.convertToCartItemsToProduct(productsInCartIds)
        cartItemsToProducts.postValue(convertedCartItemsToProducts)
        totalValueText.postValue(convertedCartItemsToProducts.getTotalValueString().formatPrice())
        isFetching.postValue(false);
    }

    fun deleteFromCart(cartItem: CartItem) {
        if (cartItem.id == null) {
            throw IllegalArgumentException(ILLEGAL_CART_DELETION)
        }

        viewModelScope.launch {
            try {
                remoteApi.deleteProductFromCart(cartItem.id)
                cartItemDeletedSuccess.postValue(
                    Triple(
                        cartItem,
                        DELETED_CART_SUCCESS,
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