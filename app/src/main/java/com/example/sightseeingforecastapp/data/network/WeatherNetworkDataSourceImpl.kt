package com.example.sightseeingforecastapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast
import com.example.sightseeingforecastapp.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val darkSkyForecastApiService: DarkSkyForecastApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<WeatherForecast>()

    override val downloadedCurrentWeather: LiveData<WeatherForecast>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try {
            val fetchedCurrentWeather = darkSkyForecastApiService
                .getCurrentWeather(location)
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity Exception", "No internet connection available.", e)
        }
    }
}