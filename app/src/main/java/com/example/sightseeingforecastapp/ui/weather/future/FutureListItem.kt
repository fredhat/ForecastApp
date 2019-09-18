package com.example.sightseeingforecastapp.ui.weather.future

import com.example.sightseeingforecastapp.R
import com.example.sightseeingforecastapp.data.db.entity.DailyWeatherForecastDetails
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_list.*
import java.text.SimpleDateFormat
import java.util.*

const val TEMP_UNIT = "°F"

class FutureListItem(
    val dailyWeatherForecastDetail: DailyWeatherForecastDetails
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_weather.text = dailyWeatherForecastDetail.summary
            updateDate()
            updateTemperature()
            updateWeatherIcon()
        }
    }

    override fun getLayout() = R.layout.item_future_list

    private fun ViewHolder.updateDate() {
        val dateFormatter = SimpleDateFormat("MM/dd/yyyy")
        val parsedDate = Date(dailyWeatherForecastDetail.time * 1000)
        textView_date.text = dateFormatter.format(parsedDate)
    }

    private fun ViewHolder.updateTemperature() {
        val avgTemp = (dailyWeatherForecastDetail.temperatureHigh + dailyWeatherForecastDetail.temperatureLow) / 2
        val formatString = "%.1f"
        val tempFormatted = String.format(formatString, avgTemp)
        textView_temperature.text = "$tempFormatted$TEMP_UNIT"
    }

    private fun ViewHolder.updateWeatherIcon() {
        val weatherIconURI = dailyWeatherForecastDetail.icon.replace("-", "_")
        val resources = imageView_weather_icon.resources
        val context = imageView_weather_icon.context
        imageView_weather_icon.setImageResource(resources.getIdentifier(
            "ic_$weatherIconURI",
            "drawable",
            context?.packageName)
        )
    }

}