package com.example.sightseeingforecastapp.data.provider

import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast

interface LocationProvider {
    suspend fun hasLocationChanged(weatherForecast: WeatherForecast): Boolean
    suspend fun getPreferredLocationString(): String
}