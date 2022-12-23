package com.iot.weather

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.iot.weather.databinding.ActivityLogBinding
import java.io.File

class LogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //take log from file log.txt
        val logFile = File(applicationContext?.filesDir, "log.txt")
        val log = logFile.readText()

        //split log by lines
        val logLines = log.split("\r ")

        // put each record in single line
        binding.logTv.text = log
    }

}