package com.aslanovaslan.forecastweathermvvm.data.provider

import com.aslanovaslan.forecastweathermvvm.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem():UnitSystem
}