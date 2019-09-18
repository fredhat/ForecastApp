package com.example.sightseeingforecastapp

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.sightseeingforecastapp.data.db.WeatherForecastDatabase
import com.example.sightseeingforecastapp.data.network.*
import com.example.sightseeingforecastapp.data.provider.LocationProvider
import com.example.sightseeingforecastapp.data.provider.LocationProviderImpl
import com.example.sightseeingforecastapp.data.repository.ForecastRepository
import com.example.sightseeingforecastapp.data.repository.ForecastRepositoryImpl
import com.example.sightseeingforecastapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.sightseeingforecastapp.ui.weather.future.FutureListViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SightseeingForecastApp : Application(), KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SightseeingForecastApp))

        bind() from singleton {
            WeatherForecastDatabase(instance())
        }

        bind() from singleton {
            instance<WeatherForecastDatabase>().weatherForecastDao()
        }

        bind() from singleton {
            instance<WeatherForecastDatabase>().currentWeatherForecastDao()
        }

        bind() from singleton {
            instance<WeatherForecastDatabase>().dailyWeatherForecastDao()
        }

        bind() from singleton {
            instance<WeatherForecastDatabase>().dailyWeatherForecastDetailsDao()
        }

        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(instance())
        }

        bind() from singleton {
            DarkSkyForecastApiService(instance())
        }

        bind<WeatherNetworkDataSource>() with singleton {
            WeatherNetworkDataSourceImpl(instance())
        }

        bind() from provider {
            LocationServices.getFusedLocationProviderClient(instance<Context>())
        }

        bind<LocationProvider>() with singleton {
            LocationProviderImpl(instance(), instance())
        }

        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance(), instance())
        }

        bind() from provider {
            CurrentWeatherViewModelFactory(instance())
        }

        bind() from provider {
            FutureListViewModelFactory(instance())
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}