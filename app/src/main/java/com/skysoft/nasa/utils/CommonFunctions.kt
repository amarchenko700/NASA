package com.skysoft.nasa.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.skysoft.nasa.R

fun hasInternet(): Boolean {
    val connectivityManager =
        App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

fun getThemeForNumber(numberCurrentTheme: Int?): Int {
    val defaultTheme = R.style.Earth
    numberCurrentTheme?.let {
        return when (it) {
            THEME_LUNAR -> R.style.Lunar
            THEME_MARTIAN -> R.style.Martian
            THEME_EARTH -> R.style.Earth
            else -> defaultTheme
        }
    }
    return defaultTheme
}