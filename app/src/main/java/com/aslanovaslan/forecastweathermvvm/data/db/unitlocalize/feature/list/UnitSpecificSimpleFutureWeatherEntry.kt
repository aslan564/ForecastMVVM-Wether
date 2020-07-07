package com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.list

import org.threeten.bp.LocalDate


interface UnitSpecificSimpleFutureWeatherEntry {
    val date: LocalDate
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
}