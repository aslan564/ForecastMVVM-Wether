package com.aslanovaslan.forecastweathermvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.aslanovaslan.forecastweathermvvm.data.provider.UnitProvider
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import com.aslanovaslan.forecastweathermvvm.internal.UnitSystem
import com.aslanovaslan.forecastweathermvvm.internal.lazyDefferd
import com.aslanovaslan.forecastweathermvvm.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository,unitProvider) {

     val weather by lazyDefferd {
        forecastRepository.getCurrentWeather(isMetricunit)
    }
}