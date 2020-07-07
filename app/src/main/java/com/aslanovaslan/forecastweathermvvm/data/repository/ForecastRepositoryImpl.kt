package com.aslanovaslan.forecastweathermvvm.data.repository

import androidx.lifecycle.LiveData
import com.aslanovaslan.forecastweathermvvm.data.db.CurrentWeatherDao
import com.aslanovaslan.forecastweathermvvm.data.db.FutureWeatherDao
import com.aslanovaslan.forecastweathermvvm.data.db.WeatherLocationDao
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.current.UnitSpecificCurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.detail.UnitSpecificeDetailFtureWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.list.UnitSpecificSimpleFutureWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.newtwork.FORCAST_DAYS_COUNT
import com.aslanovaslan.forecastweathermvvm.data.newtwork.WeatherNetworkDataSource
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.FeatureWeatherResponse
import com.aslanovaslan.forecastweathermvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {
    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }

            downloadedFutureWeather.observeForever{newFutureWeather->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastsMetric(
                startDate
            )
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out UnitSpecificeDetailFtureWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getDetailedWeatherByDateMetric(date)
            else futureWeatherDao.getDetailedWeatherByDateImperial(date)
        }
    }
    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FeatureWeatherResponse){
        fun deleteOldForecastData(){
            val today=LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }
        GlobalScope.launch (Dispatchers.IO){
            deleteOldForecastData()
            val futureWeatherList=fetchedWeather.futureWeatherEntrys.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }
    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()
        if (lastWeatherLocation == null ||
            locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }
        if (isFetcCurrentNeeded(lastWeatherLocation.zonedDateTime)) {
            fetchCurrentWeather()
        }
        if (isFetcFutureNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchFutureWeather(){
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }
    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCuerrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetcCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
    private fun isFetcFutureNeeded():Boolean{
        val today = LocalDate.now()
        val futureWeatherCount=futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount< FORCAST_DAYS_COUNT
    }
}