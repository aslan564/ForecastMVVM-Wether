package com.aslanovaslan.forecastweathermvvm.ui.weather.future.list

import androidx.lifecycle.ViewModel
import com.aslanovaslan.forecastweathermvvm.data.provider.UnitProvider
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import com.aslanovaslan.forecastweathermvvm.internal.lazyDefferd
import com.aslanovaslan.forecastweathermvvm.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel (
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider

): WeatherViewModel(forecastRepository,unitProvider) {
    // TODO: Implement the ViewModel
    val weatherEntries by lazyDefferd {
        forecastRepository.getFutureWeatherList(LocalDate.now(),super.isMetricunit)
    }
}