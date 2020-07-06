package com.aslanovaslan.forecastweathermvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.aslanovaslan.forecastweathermvvm.data.provider.UnitProvider
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import com.aslanovaslan.forecastweathermvvm.internal.UnitSystem
import com.aslanovaslan.forecastweathermvvm.internal.lazyDefferd

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    // TODO: Implement the ViewModel
    private val unitSystem = unitProvider.getUnitSystem()
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC
     val weather by lazyDefferd {
        forecastRepository.getCurrentWeather(isMetric)
    }
    val weatherLocaltion by lazyDefferd {
        forecastRepository.getWeatherLocation()
    }
}