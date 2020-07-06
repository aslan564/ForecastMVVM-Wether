package com.aslanovaslan.forecastweathermvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import com.aslanovaslan.forecastweathermvvm.internal.UnitSystem
import com.aslanovaslan.forecastweathermvvm.internal.lazyDefferd

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    // TODO: Implement the ViewModel
    private val unitSystem = UnitSystem.METRIC
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC
     val weather by lazyDefferd {
        forecastRepository.getCurrentWeather(isMetric)
    }
}