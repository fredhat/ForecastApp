package com.example.sightseeingforecastapp.data.repository

import androidx.lifecycle.LiveData
import com.example.sightseeingforecastapp.data.db.CurrentWeatherForecastDao
import com.example.sightseeingforecastapp.data.db.DailyWeatherForecastDao
import com.example.sightseeingforecastapp.data.db.DailyWeatherForecastDetailsDao
import com.example.sightseeingforecastapp.data.db.WeatherForecastDao
import com.example.sightseeingforecastapp.data.db.entity.CurrentWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails
import com.example.sightseeingforecastapp.data.network.WeatherNetworkDataSource
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast
import com.example.sightseeingforecastapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant

class ForecastRepositoryImpl (
    private val weatherForecastDao: WeatherForecastDao,
    private val currentWeatherForecastDao: CurrentWeatherForecastDao,
    private val dailyWeatherForecastDao: DailyWeatherForecastDao,
    private val dailyWeatherForecastDetailsDao: DailyWeatherForecastDetailsDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository{
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getWeatherForecast(): LiveData<WeatherForecast> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext weatherForecastDao.getWeatherForecast()
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherForecast> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherForecastDao.getCurrentWeather()
        }
    }

    override suspend fun getDailyWeather(): LiveData<DailyWeatherForecast> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext dailyWeatherForecastDao.getDailyWeather()
        }
    }

    override suspend fun getDailyWeatherDetails(startTime: Long): LiveData<List<DailyWeatherForecastDetails>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext dailyWeatherForecastDetailsDao.getDailyForecastDetails(startTime)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: WeatherForecast) {
        //fun deleteOldWeatherForecastData() {
        //    val currentTime = Instant.now().epochSecond
        //    dailyWeatherForecastDetailsDao.deleteDailyForecastDetails(currentTime)
        //}

        GlobalScope.launch(Dispatchers.IO) {
            //deleteOldWeatherForecastData()
            dailyWeatherForecastDetailsDao.insert(fetchedWeather.dailyWeatherForecast.dailyWeatherForecastDetails)
            dailyWeatherForecastDao.upsert(fetchedWeather.dailyWeatherForecast)
            currentWeatherForecastDao.upsert(fetchedWeather.currentWeatherForecast)
            weatherForecastDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData() {
        val currentTime = Instant.now().epochSecond

        val lastWeatherForecast = weatherForecastDao.getWeatherForecastNonLive()
        val lastCurrentWeatherForecast = currentWeatherForecastDao.getCurrentWeatherNonLive()
        val lastDailyWeatherForecastDetails = dailyWeatherForecastDetailsDao.getDailyForecastDetails(currentTime)

        if ((lastWeatherForecast == null)
            || (lastCurrentWeatherForecast == null)
            || (lastDailyWeatherForecastDetails == null)
            || locationProvider.hasLocationChanged(lastWeatherForecast)){
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastCurrentWeatherForecast.time))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString()
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: Long): Boolean {
        val thirtyMinutesAgo = Instant.now().minusSeconds(1800)
        return thirtyMinutesAgo.isBefore(Instant.ofEpochSecond(lastFetchTime))
    }
}