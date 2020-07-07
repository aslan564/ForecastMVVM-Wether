package com.aslanovaslan.forecastweathermvvm.data.newtwork

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.FeatureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather:LiveData<FeatureWeatherResponse>
    suspend fun fetchCuerrentWeather(
        location:String,
        languageCode:String
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    )
}