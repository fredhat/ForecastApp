package com.example.sightseeingforecastapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails

@Dao
interface DailyWeatherForecastDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailyWeatherForecastDetails: List<DailyWeatherForecastDetails>)

    @Query("select * from daily_weather_details where time >= :startTime")
    fun getDailyForecastDetails(startTime: Long): LiveData<List<DailyWeatherForecastDetails>>

    @Query("delete from daily_weather_details where time < :startTime")
    fun deleteDailyForecastDetails(startTime: Long)
}