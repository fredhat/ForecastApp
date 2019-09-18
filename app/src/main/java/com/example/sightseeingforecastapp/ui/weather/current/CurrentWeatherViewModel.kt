package com.example.sightseeingforecastapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.sightseeingforecastapp.data.repository.ForecastRepository
import com.example.sightseeingforecastapp.internal.lazyDeffered

class CurrentWeatherViewModel (
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val weather by lazyDeffered {
        forecastRepository.getCurrentWeather()
    }

    val forecast by lazyDeffered {
        forecastRepository.getWeatherForecast()
    }
}
