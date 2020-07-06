package com.aslanovaslan.forecastweathermvvm.data.newtwork

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.aslanovaslan.forecastweathermvvm.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl (
    context: Context
): ConnectivityInterceptor {
    private val appContext=context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline(appContext))
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}