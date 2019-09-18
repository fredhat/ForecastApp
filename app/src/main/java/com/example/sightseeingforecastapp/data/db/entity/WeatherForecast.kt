package com.example.sightseeingforecastapp.data.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_FORECAST_ID = 0

@Entity(tableName = "forecast")
data class WeatherForecast(
    var latitude: Double,
    var longitude: Double,
    var timezone: String,
    @Ignore
    @SerializedName("currently")
    var currentWeatherForecast: CurrentWeatherForecast,
    @Ignore
    @SerializedName("daily")
    val dailyWeatherForecast: DailyWeatherForecast
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_FORECAST_ID

    constructor() : this(
        40.730610,
        -73.935242,
        "America/New_York",
        CurrentWeatherForecast(),
        DailyWeatherForecast())
}