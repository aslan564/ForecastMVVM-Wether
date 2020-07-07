package com.aslanovaslan.forecastweathermvvm.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aslanovaslan.forecastweathermvvm.data.provider.UnitProvider
import com.aslanovaslan.forecastweathermvvm.data.repository.ForecastRepository
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModelFactory(
    private val detaildate:LocalDate,
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailWeatherViewModel(forecastRepository,detaildate,unitProvider)as T
    }
}