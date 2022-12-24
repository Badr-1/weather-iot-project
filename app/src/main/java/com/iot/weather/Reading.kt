package com.iot.weather

import java.util.*

data class Reading(
    val humidity: Double, val pressure: Double, val temperature: Double, val altitude: Double, val timestamp: Date
)