package com.iot.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val LOG_PATH = "log"

enum class ReadingLogStatus { LOADING, ERROR, DONE }
class LogViewModel : ViewModel() {

    private val _status = MutableLiveData<ReadingLogStatus>()
    val status: LiveData<ReadingLogStatus>
        get() = _status
    private val _log = MutableLiveData<List<Reading>>()
    val log: LiveData<List<Reading>>
        get() = _log

    fun loadData() {
        _status.value = ReadingLogStatus.LOADING
        try {
            val database = Firebase.database.reference
            val ref = database.child(LOG_PATH)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.value as Map<*, *>
                    val readingsLog = mutableListOf<Reading>()
                    for (entry in post.entries) {
                        val reading = entry.value.toString().split(',')
                        val timestamp = reading[0]
                        val altitude = reading[1].toDouble()
                        val pressure = reading[2].toDouble()
                        val temperature = reading[3].toDouble()
                        val humidity = reading[4].toDouble()
                        readingsLog.add(Reading(timestamp = timestamp, altitude = altitude, pressure = pressure, temperature = temperature, humidity = humidity))
                    }
                    readingsLog.sortWith(compareByDescending { it.timestamp })
                    _log.value = readingsLog
                    _status.value = ReadingLogStatus.DONE
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
            ref.addValueEventListener(listener)
        } catch (e: Exception) {
            _status.value = ReadingLogStatus.ERROR
        }
    }
}