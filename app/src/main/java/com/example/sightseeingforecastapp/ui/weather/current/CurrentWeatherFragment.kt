package com.example.sightseeingforecastapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.sightseeingforecastapp.R
import com.example.sightseeingforecastapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.floor

const val TEMP_UNIT = "°F"
const val WIND_UNIT = "mph"

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Current Forecast"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()

        val weatherForecast = viewModel.forecast.await()

        weatherForecast.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            val split = location.timezone.split("/")
            updateLocation(split[split.size - 1].replace("_", " "))
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_progressBar.visibility = View.GONE
            updateDateCurrent()
            updateTemperature(it.temperature, it.apparentTemperature)
            updateWeather(it.summary)
            updateHumidity(it.humidity)
            updatePrecipitation(it.precipProbability)
            updateWind(it.windBearing, it.windSpeed)
            updateWeatherIcon(it.icon)
        })
    }

    private fun degreesToCompass(degrees: Int): String {
        val trueDegrees = ((degrees / 22.5) + 0.5).toInt()
        val directions = listOf("N","NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW","W","WNW","NW","NNW")
        return directions[trueDegrees % 16]
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateCurrent() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temp: Double, feelsLikeTemp: Double) {
        val formatString = "%.1f"
        val tempFormatted1 = String.format(formatString, temp)
        val tempFormatted2 = String.format(formatString, feelsLikeTemp)
        textView_temperature.text = "$tempFormatted1$TEMP_UNIT"
        textView_temperature_feels.text = "Feels like $tempFormatted2$TEMP_UNIT"
    }

    private fun updateWeather(weather: String) {
        textView_weather.text = weather
    }

    private fun updateHumidity(humidity: Double) {
        val trueHumidity = floor(humidity * 100)
        textView_humidity.text = "Humidity: $trueHumidity%"
    }

    private fun updatePrecipitation(precipitationChance: Double) {
        val truePrecipitation = floor(precipitationChance * 100)
        textView_precipitation.text = "Chance of rain: $truePrecipitation%"
    }

    private fun updateWind(windDirection: Int, windSpeed: Double) {
        val trueWindDirection = degreesToCompass(windDirection)
        textView_wind.text = "Wind: $trueWindDirection, $windSpeed $WIND_UNIT"
    }

    private fun updateWeatherIcon(weatherIcon: String) {
        val weatherIconURI = weatherIcon.replace("-", "_")
        imageView_weather_icon.setImageResource(resources.getIdentifier(
            "ic_$weatherIconURI",
            "drawable",
            context?.packageName)
        )
    }

}
