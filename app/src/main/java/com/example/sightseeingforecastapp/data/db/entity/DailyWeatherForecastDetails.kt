package com.example.sightseeingforecastapp.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "daily_weather_details", indices = [Index(value = ["time"], unique = true)])
data class DailyWeatherForecastDetails(
    val time: Long,
    val summary: String,
    val icon: String,
    val temperatureHigh: Double,
    val temperatureLow: Double
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor() : this(
        0,
        "Clear",
        "clear-day",
        80.0,
        80.0)
}