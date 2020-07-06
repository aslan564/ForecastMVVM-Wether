package com.aslanovaslan.forecastweathermvvm.data.repository

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.db.CurrentWeatherDao
import com.aslanovaslan.forecastweathermvvm.data.db.WeatherLocationDao
import com.aslanovaslan.forecastweathermvvm.data.db.entity.CurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.UnitSpecificCurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.newtwork.WeatherNetworkDataSource
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import com.aslanovaslan.forecastweathermvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
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

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation=weatherLocationDao.getLocation().value
        if (lastWeatherLocation == null|| locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }
        if (isFetcCurrentNeeded(lastWeatherLocation.zonedDateTime)) {
            fetchCurrentWeather()
        }
    }
private suspend fun fetchCurrentWeather(){
    weatherNetworkDataSource.fetchCuerrentWeather(
        locationProvider.getPreferredLocationString(),
        Locale.getDefault().language
    )
}
    private fun isFetcCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}