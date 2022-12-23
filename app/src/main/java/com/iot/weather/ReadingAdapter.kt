package com.iot.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iot.weather.databinding.ReadingItemBinding

class ReadingAdapter(private val context: Context) : ListAdapter<Reading, ReadingAdapter.ReadingViewHolder>(DiffCallback) {

    class ReadingViewHolder private constructor(private var binding: ReadingItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reading: Reading) {
            binding.reading = reading
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, context: Context): ReadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReadingItemBinding.inflate(layoutInflater, parent, false)
                return ReadingViewHolder(binding, context)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Reading>() {
        override fun areItemsTheSame(oldItem: Reading, newItem: Reading): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Reading, newItem: Reading): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ReadingViewHolder {
        return ReadingViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: ReadingViewHolder, position: Int) {
        val reading = getItem(position)
        holder.bind(reading)
    }
}