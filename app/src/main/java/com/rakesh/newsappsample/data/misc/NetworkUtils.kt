package com.rakesh.newsappsample.data.misc

interface NetworkUtils {

    /**
     * Made this a function instead of a variable since it wasn't getting called every time we check networkUtils.isNetworkAvailable.
     */
    fun isNetworkAvailable(): Boolean
}