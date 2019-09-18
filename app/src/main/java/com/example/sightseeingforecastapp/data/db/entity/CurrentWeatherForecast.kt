package com.example.sightseeingforecastapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherForecast(
    val time: Long,
    val summary: String,
    val icon: String,
    val precipProbability: Double,
    val temperature: Double,
    val apparentTemperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val windBearing: Int
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID

    constructor() : this(
        0,
        "Clear",
        "clear-day",
        0.40,
        80.0,
        80.0,
        0.80,
        3.5,
        0)
}