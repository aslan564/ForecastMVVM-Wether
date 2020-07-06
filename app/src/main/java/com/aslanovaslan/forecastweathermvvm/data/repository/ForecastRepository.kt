package com.aslanovaslan.forecastweathermvvm.data.repository

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric:Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}