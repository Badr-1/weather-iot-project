package com.iot.weather
data class Reading(
    val humidity: Double, val pressure: Double, val temperature: Double, val altitude: Double, val timestamp: String
)