package com.aslanovaslan.forecastweathermvvm.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import com.aslanovaslan.forecastweathermvvm.data.provider.UnitProvider
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import com.aslanovaslan.forecastweathermvvm.internal.lazyDefferd
import com.aslanovaslan.forecastweathermvvm.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val detailDate: LocalDate,
    unitProvider: UnitProvider

) : WeatherViewModel(forecastRepository,unitProvider) {
    val weather by lazyDefferd {
        forecastRepository.getFutureWeatherByDate(detailDate,super.isMetricunit)
    }
}