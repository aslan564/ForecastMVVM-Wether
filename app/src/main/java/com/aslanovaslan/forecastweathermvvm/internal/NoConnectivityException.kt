package com.aslanovaslan.forecastweathermvvm.internal

import java.io.IOException

class NoConnectivityException :IOException()
class LocationPermissionNotGrantedException :Exception()
class DateNotFoundException :Exception()