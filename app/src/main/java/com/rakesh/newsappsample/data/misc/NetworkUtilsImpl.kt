package com.rakesh.newsappsample.data.misc

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtilsImpl(private val context: Context) : NetworkUtils {
    override fun isNetworkAvailable(): Boolean =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { cm ->
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) //Indicates that this network should be able to reach the internet
                        && hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) //Indicates that connectivity on this network was successfully validated
                        && (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        || hasTransport(NetworkCapabilities.TRANSPORT_VPN))
            } ?: false
        } ?: false
}