package com.oyelekeokiki.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(private val remoteApi: RemoteApi): ViewModel() {
    init {
        viewModelScope.launch {
            try {
                val cartItems = remoteApi.getProducts()
                Log.e("Cart Items", cartItems.toString());
            } catch (e: Exception) {
                Log.e("Cart Error", e.localizedMessage);
            }
        }
    }
}