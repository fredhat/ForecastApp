package com.example.sightseeingforecastapp.ui.weather.future

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sightseeingforecastapp.R
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails
import com.example.sightseeingforecastapp.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FutureListFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FutureListViewModelFactory by instance()
    private lateinit var viewModel: FutureListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FutureListViewModel::class.java)
        // TODO: Use the ViewModel

        (activity as? AppCompatActivity)?.supportActionBar?.title = "7 Day Forecast"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main){
        val weatherDetails = viewModel.details.await()

        val weatherForecast = viewModel.forecast.await()

        weatherForecast.observe(this@FutureListFragment, Observer { location ->
            if (location == null) return@Observer
            val split = location.timezone.split("/")
            updateLocation(split[split.size - 1].replace("_", " "))
        })

        weatherDetails.observe(this@FutureListFragment, Observer { weatherDetails ->
            if (weatherDetails == null) return@Observer

            group_progressBar.visibility = View.GONE
            updateDateWeek()
            initRecyclerView(weatherDetails.toFutureWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

    private fun List<DailyWeatherForecastDetails>.toFutureWeatherItems() : List<FutureListItem> {
        return this.map {
            FutureListItem(it)
        }
    }

    private fun initRecyclerView(items: List<FutureListItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureListFragment.context)
            adapter = groupAdapter
        }
    }

}
