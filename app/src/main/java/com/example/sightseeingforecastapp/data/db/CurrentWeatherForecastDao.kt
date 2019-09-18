package com.example.sightseeingforecastapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sightseeingforecastapp.data.db.entity.CURRENT_WEATHER_ID
import com.example.sightseeingforecastapp.data.db.entity.CurrentWeatherForecast

@Dao
interface CurrentWeatherForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherForecast: CurrentWeatherForecast)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherForecast>

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherNonLive(): CurrentWeatherForecast?
}