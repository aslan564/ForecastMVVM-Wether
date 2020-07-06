package com.aslanovaslan.forecastweathermvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aslanovaslan.forecastweathermvvm.R
import com.aslanovaslan.forecastweathermvvm.internal.glide.GlideApp
import com.aslanovaslan.forecastweathermvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        // TODO: Use the ViewModel
        binUi()

        /*
        val apiService =
            WeatherApiService(ConnectivityInterceptorImpl(this.requireContext()))

         val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
          weatherNetworkDataSource.downloadedCurrentWeather.observe(
              this.viewLifecycleOwner,
              Observer {
                  text_wiew3.text = it.currentWeatherEntry.toString()

              })
          GlobalScope.launch(Dispatchers.Main) {
              weatherNetworkDataSource.fetchCuerrentWeather("Paris", "en")
              //  println(weatherResponse.location.toString())
          }*/

    }

    private fun binUi() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocaltion.await()
        weatherLocation.observe(this@CurrentWeatherFragment.viewLifecycleOwner, Observer {location->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment.viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE


            updateDayToday()
            updateTemperature(it.temperature, it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePercipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisiblity(it.visibilityDistance)
            GlideApp.with(this@CurrentWeatherFragment)
                .load("http:${it.conditionIconUrl}")
                .into(imageView_condition_icon)
        })
    }

    private fun choseUnitLocalizedUnitAbbrevation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDayToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unitAbbrevation = choseUnitLocalizedUnitAbbrevation(" C", " F")
        textView_temperature.text = "$temperature$unitAbbrevation"
        textView_feels_like_temperature.text = "Feels Like $feelsLike$unitAbbrevation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePercipitation(precipitationVolume: Double) {
        val unitAbbrevation = choseUnitLocalizedUnitAbbrevation(" mm", " in")
        textView_precipitation.text = "Percipitation $precipitationVolume $unitAbbrevation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbrevation = choseUnitLocalizedUnitAbbrevation(" khp", " mhp")
        textView_wind.text = "Wind $windDirection $windSpeed $unitAbbrevation"
    }

    private fun updateVisiblity(visiblityDistance: Double) {
        val unitAbbrevation = choseUnitLocalizedUnitAbbrevation(" km", " mi")
        textView_visibility.text = "Visiblity $visiblityDistance $unitAbbrevation"
    }

}