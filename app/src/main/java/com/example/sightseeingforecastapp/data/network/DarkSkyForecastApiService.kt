package com.example.sightseeingforecastapp.data.network

import com.example.sightseeingforecastapp.data.db.entity.WeatherForecast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://api.darksky.net/forecast"
const val API_KEY = "9da436db03f470e79adcf14ce1cb41f6"

interface DarkSkyForecastApiService {

    @GET("{location}")
    suspend fun getCurrentWeather(
        @Path("location", encoded = true) location: String
    ): WeatherForecast

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): DarkSkyForecastApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    //This reduces the size of the response object, increasing performance
                    .addQueryParameter("exclude","minutely,hourly,alerts,flags")
                    //This is actually the default value but can be changed later for better user experience
                    .addQueryParameter("lang","en")
                    //This is actually the default value but can be changed later for better user experience
                    .addQueryParameter("units","us")
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl("$BASE_URL/$API_KEY/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DarkSkyForecastApiService::class.java)
        }
    }
}