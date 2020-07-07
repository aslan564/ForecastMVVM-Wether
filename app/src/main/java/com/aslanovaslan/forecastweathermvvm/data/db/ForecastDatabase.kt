package com.aslanovaslan.forecastweathermvvm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aslanovaslan.forecastweathermvvm.data.db.entity.CurrentWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.entity.FutureWeatherEntry
import com.aslanovaslan.forecastweathermvvm.data.db.entity.WeatherLocation
import okhttp3.internal.Internal.instance

@Database(
    entities = [CurrentWeatherEntry::class,FutureWeatherEntry::class,WeatherLocation::class],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {
        @Volatile private var instance:ForecastDatabase?=null
        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ForecastDatabase::class.java,
            "futureWeatherEntries.db"
        ).build()
    }
}