package com.iot.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iot.weather.databinding.ActivityMainBinding
import java.util.*

private const val HIGH_TEMPERATURE = 25.0
private const val LOW_TEMPERATURE = 10.0
private const val HIGH_HUMIDITY = 80.0
private const val LOW_HUMIDITY = 20.0
private const val HIGH_PRESSURE = 1000.0
private const val LOW_PRESSURE = 900.0
private const val HIGH_ALTITUDE = 1000.0
private const val LOW_ALTITUDE = 0.0
private const val PATH = "status"
private const val TEMPERATURE = "temperature"
private const val PRESSURE = "pressure"
private const val HUMIDITY = "humidity"
private const val ALTITUDE = "altitude"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // read data from firebase
        val database = Firebase.database.reference
        val myRef = database.child(PATH)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.value as Map<*, *>
                if (post.entries.size == 4) {
                    // get current time
                    val currentTime = Date(System.currentTimeMillis())
                    "Last update: $currentTime".also { binding.textLastUpdate.text = it }
                    "${post[TEMPERATURE]}\u2103".also { binding.textTemperatureLarge.text = it }
                    "${post[TEMPERATURE]}\u2103".also { binding.textTemperature.text = it }
                    "${post[HUMIDITY]}".also { binding.textHumidity.text = it }
                    "${post[PRESSURE]} hPa".also { binding.textPressure.text = it }
                    "${post[ALTITUDE]} m".also { binding.textAltitude.text = it }


                    // create alert dialog
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("Warning")
                    val show: Boolean
                    val temperature = post[TEMPERATURE] as Double
                    val humidity = post[HUMIDITY] as Double
                    val pressure = post[PRESSURE] as Double
                    val altitude = post[ALTITUDE] as Double
                    val msg: String = when {
                        temperature > HIGH_TEMPERATURE -> "Temperature is too high"
                        temperature < LOW_TEMPERATURE -> "Temperature is too low"
                        humidity > HIGH_HUMIDITY -> "Humidity is too high"
                        humidity < LOW_HUMIDITY -> "Humidity is too low"
                        pressure > HIGH_PRESSURE -> "Pressure is too high"
                        pressure < LOW_PRESSURE -> "Pressure is too low"
                        altitude > HIGH_ALTITUDE -> "Altitude is too high"
                        altitude < LOW_ALTITUDE -> "Altitude is too low"
                        else -> {
                            "Everything is OK"
                        }
                    }
                    show = msg != "Everything is OK"
                    alertDialog.setMessage(msg)
                    alertDialog.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    // show alert dialog
                    if (show) {
                        alertDialog.show()
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        myRef.addValueEventListener(postListener)
    }
}