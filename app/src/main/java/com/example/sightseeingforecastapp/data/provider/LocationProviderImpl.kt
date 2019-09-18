package com.example.sightseeingforecastapp.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast
import com.example.sightseeingforecastapp.internal.LocationPermissionNotGrantedException
import com.example.sightseeingforecastapp.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.lang.NumberFormatException
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LATITUDE = "CUSTOM_LATITUDE"
const val CUSTOM_LONGITUDE = "CUSTOM_LONGITUDE"
val latitudeRegex = Regex("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)\$")
val longitudeRegex = Regex("^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)\$")

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(weatherForecast: WeatherForecast): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(weatherForecast)
        } catch (e: LocationPermissionNotGrantedException){
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(weatherForecast)
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationString()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException){
                return "${getCustomLocationString()}"
            }
        } else
            return "${getCustomLocationString()}"
    }

    private suspend fun hasDeviceLocationChanged(weatherForecast: WeatherForecast): Boolean {
        if (!isUsingDeviceLocation())
            return false
        val deviceLocation = getLastDeviceLocation().await()
            ?: return false
        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - weatherForecast.latitude) > comparisonThreshold &&
                abs(deviceLocation.longitude - weatherForecast.longitude) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(weatherForecast: WeatherForecast): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationString = getCustomLocationString()
            val formatString = "%.4f"
            val latitude = String.format(formatString, weatherForecast.latitude)
            val longitude = String.format(formatString, weatherForecast.longitude)
            return customLocationString != "$latitude,$longitude"
        }
        return false
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationString(): String? {
        val longitude = getCustomLatitude()
        val latitude = getCustomLongitude()
        return "$longitude,$latitude"
    }

    private fun getCustomLatitude(): String? {
        val formatString = "%.4f"
        try {
            val latitude = (preferences.getString(CUSTOM_LATITUDE, "0.0"))?.toDouble()
                ?: return "0.0000"
            val formattedString = String.format(formatString, latitude)
            if (latitudeRegex.matches(formattedString))
                return formattedString
            return "0.0000"
        }catch (e: NumberFormatException){
            return "0.0000"
        }
    }

    private fun getCustomLongitude(): String? {
        val formatString = "%.4f"
        try {
            val longitude = (preferences.getString(CUSTOM_LONGITUDE, "0.0"))?.toDouble()
                ?: return "0.0000"
            val formattedString = String.format(formatString, longitude)
            if (longitudeRegex.matches(formattedString))
                return formattedString
            return "0.0000"
        }catch (e: NumberFormatException){
            return "0.0000"
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}