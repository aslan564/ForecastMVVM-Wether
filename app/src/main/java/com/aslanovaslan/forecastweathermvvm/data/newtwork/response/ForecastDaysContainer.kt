package com.aslanovaslan.forecastweathermvvm.data.newtwork.response


import com.aslanovaslan.forecastweathermvvm.data.db.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)