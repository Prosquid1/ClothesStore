package com.oyelekeokiki.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyelekeokiki.networking.RemoteApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val remoteApi: RemoteApi): ViewModel() {
    init {
        viewModelScope.launch {
            try {
                val products = remoteApi.getProducts()
            } catch (e: Exception) {
                Log.e("Cart Error", e.localizedMessage);
            }
        }
    }
}