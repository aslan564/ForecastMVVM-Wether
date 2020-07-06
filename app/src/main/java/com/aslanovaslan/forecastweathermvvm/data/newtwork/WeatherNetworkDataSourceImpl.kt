package com.aslanovaslan.forecastweathermvvm.data.newtwork

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import com.aslanovaslan.forecastweathermvvm.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather= MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCuerrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather =
                weatherApiService.getCurrentWeather(location, languageCode)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet Connection: ",e )
        }
    }
}