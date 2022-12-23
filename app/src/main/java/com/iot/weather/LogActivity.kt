package com.iot.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.iot.weather.databinding.ActivityLogBinding


class LogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding
    private val viewModel: LogViewModel by lazy {
        ViewModelProvider(this)[LogViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // read from bundle
        binding = ActivityLogBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        viewModel.loadData()
        binding.viewModel = viewModel
        binding.readingRecycler.adapter = ReadingAdapter()
        setContentView(binding.root)

    }
}