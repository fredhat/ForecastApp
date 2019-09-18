package com.example.sightseeingforecastapp.data.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val DAILY_WEATHER_ID = 0

@Entity(tableName = "daily_weather")
data class DailyWeatherForecast(
    var summary: String,
    var icon: String,
    @Ignore
    @SerializedName("data")
    val dailyWeatherForecastDetails: List<DailyWeatherForecastDetails>
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = DAILY_WEATHER_ID

    constructor() : this(
        "Clear",
        "clear-day",
        emptyList())
}