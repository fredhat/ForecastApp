package com.example.sightseeingforecastapp.internal

import java.io.IOException

class NoConnectivityException: IOException()

class LocationPermissionNotGrantedException: Exception()