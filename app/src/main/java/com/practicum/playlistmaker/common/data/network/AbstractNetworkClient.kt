package com.practicum.playlistmaker.common.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

abstract class AbstractNetworkClient(
    private val connectivityManager: ConnectivityManager
) : NetworkClient {

    protected fun isConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        Log.i(TAG, "No internet access")
        return false
    }

    companion object {
        private val TAG = AbstractNetworkClient::class.java.simpleName
    }
}