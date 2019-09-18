package com.example.sightseeingforecastapp.ui.weather.future

import androidx.lifecycle.ViewModel
import com.example.sightseeingforecastapp.data.repository.ForecastRepository
import com.example.sightseeingforecastapp.internal.lazyDeffered
import org.threeten.bp.Instant

class FutureListViewModel (
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val details by lazyDeffered {
        forecastRepository.getDailyWeatherDetails(Instant.now().epochSecond)
    }

    val forecast by lazyDeffered {
        forecastRepository.getWeatherForecast()
    }
}