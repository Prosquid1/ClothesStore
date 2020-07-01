package com.oyelekeokiki.networking

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Checks the Internet connection and performs an action if it's active.
 */
class NetworkStatusChecker(private val connectivityManager: ConnectivityManager?) {

  inline fun performIfConnectedToInternet(action: () -> Unit) {
    val hasInternetConnection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) hasInternetConnection() else deprecatedIsNetworkAvailable();
    if (hasInternetConnection) {
      action()
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun hasInternetConnection(): Boolean {
    val network = connectivityManager?.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
      || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
      || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
  }

  @Suppress("DEPRECATION")
  fun deprecatedIsNetworkAvailable(): Boolean {
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
  }
}
