package com.example.sightseeingforecastapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sightseeingforecastapp.data.db.entity.CurrentWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecast
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast

@Database(
    entities = [WeatherForecast::class, CurrentWeatherForecast::class, DailyWeatherForecast::class, DailyWeatherForecastDetails::class],
    version = 1
)
abstract class WeatherForecastDatabase : RoomDatabase() {
    abstract fun weatherForecastDao(): WeatherForecastDao

    abstract fun currentWeatherForecastDao(): CurrentWeatherForecastDao

    abstract fun dailyWeatherForecastDao(): DailyWeatherForecastDao

    abstract fun dailyWeatherForecastDetailsDao(): DailyWeatherForecastDetailsDao

    companion object {
        @Volatile private var instance: WeatherForecastDatabase? = null
        private val LOCK = Any()

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherForecastDatabase::class.java, "weather_forecast.db")
                .build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{ instance = it }
        }
    }
}