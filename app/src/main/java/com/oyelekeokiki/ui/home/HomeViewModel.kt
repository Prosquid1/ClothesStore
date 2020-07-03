package com.oyelekeokiki.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.model.Failure
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.model.Success
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val remoteApi: RemoteApi): ViewModel() {
    var products: MutableLiveData<List<Product>>? = null
    var errorMessage: MutableLiveData<String>? = null
    var isFetching: MutableLiveData<Boolean>? = null

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            isFetching?.postValue(true);
            try {
                val result = remoteApi.getProducts()
                if (result is Success) {
                    products?.postValue(result.data)
                } else if (result is Failure){
                    errorMessage?.postValue(result.error?.localizedMessage)
                }
                isFetching?.postValue(false);
            } catch (e: Exception) {
                errorMessage?.postValue(e.localizedMessage)
                isFetching?.postValue(false);
            }
        }
    }
}