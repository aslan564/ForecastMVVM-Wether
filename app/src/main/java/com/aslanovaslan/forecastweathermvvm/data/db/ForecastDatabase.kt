package com.aslanovaslan.forecastweathermvvm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aslanovaslan.forecastweathermvvm.data.db.entity.CurrentWeatherEntry
import okhttp3.internal.Internal.instance

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile private var instance:ForecastDatabase?=null
        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ForecastDatabase::class.java,
            "forecastweather.db"
        ).build()
    }
}