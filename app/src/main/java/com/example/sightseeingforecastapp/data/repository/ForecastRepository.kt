package com.example.sightseeingforecastapp.data.repository

import androidx.lifecycle.LiveData
import com.example.sightseeingforecastapp.data.db.entity.CurrentWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast

interface ForecastRepository {
    suspend fun getWeatherForecast(): LiveData<WeatherForecast>

    suspend fun getCurrentWeather(): LiveData<CurrentWeatherForecast>

    suspend fun getDailyWeather(): LiveData<DailyWeatherForecast>

    suspend fun getDailyWeatherDetails(startTime: Long): LiveData<List<DailyWeatherForecastDetails>>
}