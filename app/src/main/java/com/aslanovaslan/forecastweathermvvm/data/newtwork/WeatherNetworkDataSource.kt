package com.aslanovaslan.forecastweathermvvm.data.newtwork

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
    suspend fun fetchCuerrentWeather(
        location:String,
        languageCode:String
    )
}