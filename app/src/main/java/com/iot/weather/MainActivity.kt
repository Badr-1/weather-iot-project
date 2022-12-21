@file:Suppress("DEPRECATION")

package com.iot.weather

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iot.weather.databinding.ActivityMainBinding
import com.iot.weather.utils.UserSettings
import java.util.*


private const val HIGH_TEMPERATURE = 25.0
private const val HIGH_HUMIDITY = 80.0
private const val HIGH_PRESSURE = 1000.0
private const val HIGH_ALTITUDE = 1000.0
private const val PATH = "status"
private const val TEMPERATURE = "temperature"
private const val PRESSURE = "pressure"
private const val HUMIDITY = "humidity"
private const val ALTITUDE = "altitude"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userSettings: UserSettings
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSettings()

        getUserSettings()

        readFromDatabase()

        binding.settingsButton.setOnClickListener {
            val settingsDialog = SettingsDialog.newInstance(userSettings)
            settingsDialog.show(supportFragmentManager, "SettingsDialog")
        }
    }

    private fun getUserSettings() {
        supportFragmentManager.setFragmentResultListener("userSettings", this) { _, bundle ->
            val result: UserSettings = bundle.getSerializable("userSettings") as UserSettings
            Snackbar.make(this, binding.root, "Settings saved", Snackbar.LENGTH_SHORT).show()
            userSettings = result
        }
    }

    private fun readFromDatabase() {
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
                        temperature > userSettings.temperatureThreshold -> "Temperature is too high"
                        humidity > userSettings.humidityThreshold -> "Humidity is too high"
                        pressure > userSettings.pressureThreshold -> "Pressure is too high"
                        altitude > userSettings.altitudeThreshold -> "Altitude is too high"
                        else -> {
                            "Everything is OK"
                        }
                    }
                    show = msg != "Everything is OK"
                    /* val mail = """
                             $msg

                             Temperature: $temperature ℃
                             Humidity: $humidity
                             Pressure: $pressure hPa
                             Altitude: $altitude m
                         """.trimIndent()*/
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
                        /*val mailAPI =
                            MailAPI(userSettings.email, "Weather IOT App", mail)
                        mailAPI.execute()*/
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        myRef.addValueEventListener(postListener)
    }

    private fun loadSettings() {
        sharedPreferences =
            applicationContext.getSharedPreferences("sharedPreferences", MODE_PRIVATE)

        if (sharedPreferences.contains("E-mail") && sharedPreferences.contains("Temperature") && sharedPreferences.contains(
                "Pressure"
            ) && sharedPreferences.contains("Humidity") && sharedPreferences.contains("Altitude")
        ) {
            userSettings = UserSettings(
                sharedPreferences.getString("E-mail", "").toString(),
                sharedPreferences.getString("Pressure", "").toString().toDouble(),
                sharedPreferences.getString("Temperature", "").toString().toDouble(),
                sharedPreferences.getString("Humidity", "").toString().toDouble(),
                sharedPreferences.getString("Altitude", "").toString().toDouble()
            )
        } else {
            userSettings = UserSettings(
                "rick.sanchez4044@gmail.com",
                HIGH_PRESSURE,
                HIGH_TEMPERATURE,
                HIGH_HUMIDITY,
                HIGH_ALTITUDE
            )
        }
    }
}