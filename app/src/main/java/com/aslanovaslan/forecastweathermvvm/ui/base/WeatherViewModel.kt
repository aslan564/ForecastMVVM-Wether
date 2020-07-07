package com.aslanovaslan.forecastweathermvvm.ui.base

import androidx.lifecycle.ViewModel
import com.aslanovaslan.forecastweathermvvm.data.provider.UnitProvider
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import com.aslanovaslan.forecastweathermvvm.internal.UnitSystem
import com.aslanovaslan.forecastweathermvvm.internal.lazyDefferd

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
): ViewModel() {
    private val unitSystem=unitProvider.getUnitSystem()
    val isMetricunit:Boolean
    get()=unitSystem==UnitSystem.METRIC
    val weatherLocation by lazyDefferd {
        forecastRepository.getWeatherLocation()
    }
}