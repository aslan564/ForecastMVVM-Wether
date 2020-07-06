package com.aslanovaslan.forecastweathermvvm.data.newtwork.response

import com.aslanovaslan.forecastweathermvvm.data.db.entity.CurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)