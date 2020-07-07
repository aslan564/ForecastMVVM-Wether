package com.aslanovaslan.forecastweathermvvm.data.newtwork

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.FeatureWeatherResponse
import com.aslanovaslan.forecastweathermvvm.internal.NoConnectivityException
const val FORCAST_DAYS_COUNT=7
class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCuerrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather =
                weatherApiService.getCurrentWeather(location, languageCode)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet Connection: ", e)
        }
    }

    private val _downloadedFeatureWeather = MutableLiveData<FeatureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FeatureWeatherResponse>
        get() = _downloadedFeatureWeather

    override suspend fun fetchFutureWeather(location: String, languageCode: String) {
        try {
            val fetchedFeatureWeather=weatherApiService.getFutureWeather(location,
                FORCAST_DAYS_COUNT
                , languageCode)
                .await()
            _downloadedFeatureWeather.postValue(fetchedFeatureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection : ", e)
        }
    }
}