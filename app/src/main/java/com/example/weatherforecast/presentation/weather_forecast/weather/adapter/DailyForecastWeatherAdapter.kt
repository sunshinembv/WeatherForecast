package com.example.weatherforecast.presentation.weather_forecast.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ItemDailyForecastWeatherBinding
import com.example.weatherforecast.presentation.weather_forecast.weather.ui_model.DailyForecastWeatherItemUI

class DailyForecastWeatherAdapter :
    RecyclerView.Adapter<DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder>() {

    private val differ = AsyncListDiffer(this, DailyForecastWeatherDiffUtilCallback())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyForecastWeatherViewHolder {
        return DailyForecastWeatherViewHolder(
            ItemDailyForecastWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyForecastWeatherViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun updateForecastWeatherList(newDailyForecastWeatherItemListUI: List<DailyForecastWeatherItemUI>) {
        differ.submitList(newDailyForecastWeatherItemListUI)
    }

    class DailyForecastWeatherViewHolder(
        private val itemWeatherBinding: ItemDailyForecastWeatherBinding
    ) : RecyclerView.ViewHolder(itemWeatherBinding.root) {
        fun bind(itemUI: DailyForecastWeatherItemUI) {
            with(itemWeatherBinding) {
                weatherDate.text = itemUI.date
                weatherDayOfWeek.text = itemUI.dayOfWeek
                weatherDayDegrees.text =
                    itemView.context.getString(R.string.temperature, itemUI.dayDegrees)
                weatherNightDegrees.text = itemView.context.getString(
                    R.string.temperature,
                    itemUI.nightDegrees
                )

                Glide.with(itemView)
                    .load(itemView.context.getString(R.string.icon_url, itemUI.icon))
                    .error(R.drawable.ic_download_error)
                    .placeholder(R.drawable.ic_downloading).into(weatherSmallIcon)
            }
        }
    }

    class DailyForecastWeatherDiffUtilCallback() :
        DiffUtil.ItemCallback<DailyForecastWeatherItemUI>() {
        override fun areItemsTheSame(
            oldItem: DailyForecastWeatherItemUI,
            newItem: DailyForecastWeatherItemUI
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: DailyForecastWeatherItemUI,
            newItem: DailyForecastWeatherItemUI
        ): Boolean {
            return oldItem == newItem
        }

    }
}
