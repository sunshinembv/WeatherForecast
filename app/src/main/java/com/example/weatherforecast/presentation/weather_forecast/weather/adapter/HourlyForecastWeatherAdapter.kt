package com.example.weatherforecast.presentation.weather_forecast.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemHourlyForecastWeatherBinding
import com.example.weatherforecast.presentation.weather_forecast.weather.ui_model.HourlyForecastWeatherItemUI

class HourlyForecastWeatherAdapter :
    RecyclerView.Adapter<HourlyForecastWeatherAdapter.HourlyForecastWeatherViewHolder>() {

    private val differ = AsyncListDiffer(this, HourlyForecastWeatherDiffUtilCallback())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyForecastWeatherViewHolder {
        return HourlyForecastWeatherViewHolder(
            ItemHourlyForecastWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyForecastWeatherViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun updateForecastWeatherList(newHourlyForecastWeatherItemUI: List<HourlyForecastWeatherItemUI>) {
        differ.submitList(newHourlyForecastWeatherItemUI)
    }

    class HourlyForecastWeatherViewHolder(private val itemWeatherBinding: ItemHourlyForecastWeatherBinding) :
        RecyclerView.ViewHolder(itemWeatherBinding.root) {

        fun bind(itemUI: HourlyForecastWeatherItemUI) {
            with(itemWeatherBinding) {
                time.text = itemUI.time
                hourlyTemperature.text = itemView.context.getString(
                    R.string.temperature,
                    itemUI.temperature
                )
                Glide.with(itemView)
                    .load(itemView.context.getString(R.string.icon_url, itemUI.icon))
                    .error(R.drawable.ic_download_error)
                    .placeholder(R.drawable.ic_downloading).into(weatherSmallIcon)
            }
        }
    }

    class HourlyForecastWeatherDiffUtilCallback :
        DiffUtil.ItemCallback<HourlyForecastWeatherItemUI>() {
        override fun areItemsTheSame(
            oldItem: HourlyForecastWeatherItemUI,
            newItem: HourlyForecastWeatherItemUI
        ): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(
            oldItem: HourlyForecastWeatherItemUI,
            newItem: HourlyForecastWeatherItemUI
        ): Boolean {
            return oldItem == newItem
        }

    }
}
