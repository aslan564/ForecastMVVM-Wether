package com.aslanovaslan.forecastweathermvvm.data.repository

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.db.CurrentWeatherDao
import com.aslanovaslan.forecastweathermvvm.data.db.entity.CurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.UnitSpecificCurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.newtwork.WeatherNetworkDataSource
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData() {
        if (isFetcCurrentNeeded(ZonedDateTime.now().minusHours(1))) {
            fetchCurrentWeather()
        }
    }
private suspend fun fetchCurrentWeather(){
    weatherNetworkDataSource.fetchCuerrentWeather(
        "Paris",
        Locale.getDefault().language
    )
}
    private fun isFetcCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}