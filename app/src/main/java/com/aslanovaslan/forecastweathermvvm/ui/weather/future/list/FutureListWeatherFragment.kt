@file:Suppress("DEPRECATION")

package com.aslanovaslan.forecastweathermvvm.ui.weather.future.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import androidx.recyclerview.widget.LinearLayoutManager
import com.aslanovaslan.forecastweathermvvm.R
import com.aslanovaslan.forecastweathermvvm.data.db.LocalDateConverter
import com.aslanovaslan.forecastweathermvvm.data.db.unitlocalize.feature.list.UnitSpecificSimpleFutureWeatherEntry
import com.aslanovaslan.forecastweathermvvm.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class FutureListWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FutureListWeatherViewModelFactory by instance()
    private lateinit var viewModel: FutureListWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FutureListWeatherViewModel::class.java)
        bindUi()
    }

    private fun bindUi() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(
            this@FutureListWeatherFragment.viewLifecycleOwner,
            Observer { location ->
                if (location == null) return@Observer
                updateLocation(location.name)
            })
        futureWeatherEntries.observe(
            this@FutureListWeatherFragment.viewLifecycleOwner,
            Observer { weatherEntries ->
                group_loading.visibility = View.GONE
                updateDateToNextWeek()
                initRecyclerWiew(weatherEntries.toFutureWeatherItems())
            })
    }



    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toFutureWeatherItems(): List<FeatureWeatherItem> {
        return this.map {
            FeatureWeatherItem(it)
        }
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun initRecyclerWiew(items: List<FeatureWeatherItem>) {
        val groupAdapter=GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply {
            layoutManager=LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter=groupAdapter
        }
        groupAdapter.setOnItemClickListener{item, view ->
            (item as? FeatureWeatherItem)?.let {
                showWeatherDetail(it.weatherEntry.date,view)
            }
        }
    }
    private fun showWeatherDetail(date: LocalDate, view: View) {
        val dateString=LocalDateConverter.dateToString(date)!!
            val actionDetail= FutureListWeatherFragmentDirections.actionFeatureWeatherFragmentToFutureDetailWeatherFragment(dateString)
            Navigation.findNavController(view).navigate(actionDetail)
    }
}