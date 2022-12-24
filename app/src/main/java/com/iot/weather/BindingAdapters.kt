package com.iot.weather

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Reading>?) {
    val adapter = recyclerView.adapter as ReadingAdapter
    adapter.submitList(data)
}

@BindingAdapter("temperatureText")
fun bindTextViewToDisplayTemperature(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.temperature_format), number)
}

@BindingAdapter("pressureText")
fun bindTextViewToDisplayPressure(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.pressure_format), number)
}

@BindingAdapter("altitudeText")
fun bindTextViewToDisplayAltitude(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.altitude_format), number)
}

@BindingAdapter("humidityText")
fun bindTextViewToDisplayHumidity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.humidity_format), number)

}

@BindingAdapter("timeStampText")
fun bindTextViewToDisplayTimeStamp(textView: TextView, time: Date) {
    textView.text = time.toString()

}

@BindingAdapter("readingLogStatus")
fun bindStatus(progressBar: ProgressBar, status: ReadingLogStatus?) {
    when (status) {
        ReadingLogStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        ReadingLogStatus.ERROR -> {
            Toast.makeText(progressBar.context, "Failed To Retrieve The Data", Toast.LENGTH_SHORT).show()
        }

        else -> progressBar.visibility = View.GONE

    }
}