package com.iot.weather.utils

import java.io.Serializable

data class UserSettings(
    val email: String,
    val pressureThreshold: Double,
    val temperatureThreshold: Double,
    val humidityThreshold: Double,
    val altitudeThreshold: Double
) : Serializable