package com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.detail

import org.threeten.bp.LocalDate

interface UnitSpecificeDetailFtureWeatherEntry {
    val date: LocalDate
    val maxTemperature: Double
    val minTemperature: Double
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val maxWindSpeed: Double
    val totalPrecipitation: Double
    val avgVisibilityDistance: Double
    val uv: Double
}