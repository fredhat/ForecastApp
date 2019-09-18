package com.example.sightseeingforecastapp.data.network

import androidx.lifecycle.LiveData
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<WeatherForecast>

    suspend fun fetchCurrentWeather(
        location: String
    )
}