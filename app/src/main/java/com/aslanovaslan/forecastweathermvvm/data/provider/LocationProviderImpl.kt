package com.aslanovaslan.forecastweathermvvm.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import com.aslanovaslan.forecastweathermvvm.internal.LocationPermissionNotGrantedException
import com.aslanovaslan.forecastweathermvvm.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.util.jar.Manifest

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOME_LOCATION = "CUSTOME_LOCATION"

class LocationProviderImpl(
    context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomeLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomeLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e:LocationPermissionNotGrantedException) {
                return "${getCustomeLocationName()}"
            }
        }else
            return "${getCustomeLocationName()}"
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        val compairsonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > compairsonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > compairsonThreshold
    }

    private fun hasCustomeLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val customLocationName = getCustomeLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomeLocationName(): String? {
        return preferences.getString(CUSTOME_LOCATION, null)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        if (hasLocationPermission())
            return fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}