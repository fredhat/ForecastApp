package com.example.sightseeingforecastapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sightseeingforecastapp.data.db.entity.DAILY_WEATHER_ID
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails

@Dao
interface DailyWeatherForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(dailyWeatherForecast: DailyWeatherForecast)

    @Query("select * from daily_weather where id = $DAILY_WEATHER_ID")
    fun getDailyWeather(): LiveData<DailyWeatherForecast>
}