package com.example.sightseeingforecastapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sightseeingforecastapp.data.db.entity.CURRENT_FORECAST_ID
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast

@Dao
interface WeatherForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherForecast: WeatherForecast)

    @Query("select * from forecast where id = $CURRENT_FORECAST_ID")
    fun getWeatherForecast(): LiveData<WeatherForecast>

    @Query("select * from forecast where id = $CURRENT_FORECAST_ID")
    fun getWeatherForecastNonLive(): WeatherForecast?
}