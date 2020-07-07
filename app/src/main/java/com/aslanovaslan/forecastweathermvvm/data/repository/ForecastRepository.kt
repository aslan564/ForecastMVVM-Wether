package com.aslanovaslan.forecastweathermvvm.data.repository

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.current.UnitSpecificCurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.detail.UnitSpecificeDetailFtureWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.list.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(metric:Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getFutureWeatherList(startDate: LocalDate,metric: Boolean):LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
    suspend fun getFutureWeatherByDate(date: LocalDate,metric: Boolean):LiveData<out UnitSpecificeDetailFtureWeatherEntry>
    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}