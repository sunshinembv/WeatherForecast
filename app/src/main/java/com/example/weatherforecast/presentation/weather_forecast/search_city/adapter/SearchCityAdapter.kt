package com.example.weatherforecast.presentation.weather_forecast.search_city.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.ItemSearchCityBinding
import com.example.weatherforecast.presentation.weather_forecast.search_city.ui_model.SearchCityItemUI
import com.example.weatherforecast.presentation.weather_forecast.weather.view_model.CityForecastWeatherViewModel.Companion.TOKYO_NERIMA_LATITUDE_DEFAULT
import com.example.weatherforecast.presentation.weather_forecast.weather.view_model.CityForecastWeatherViewModel.Companion.TOKYO_NERIMA_LONGITUDE_DEFAULT

class SearchCityAdapter(private val chooseCityCallback: (latitude: Double, longitude: Double) -> Unit) :
    RecyclerView.Adapter<SearchCityAdapter.SearchCityViewHolder>() {

    private val differ = AsyncListDiffer(this, SearchCityDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        return SearchCityViewHolder(
            ItemSearchCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            chooseCityCallback
        )
    }

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun updateCityList(newCityList: List<SearchCityItemUI>) {
        differ.submitList(newCityList)
    }

    class SearchCityViewHolder(
        private val itemSearchCityBinding: ItemSearchCityBinding,
        private val chooseCityCallback: (latitude: Double, longitude: Double) -> Unit
    ) : RecyclerView.ViewHolder(itemSearchCityBinding.root) {
        private var latitude: Double? = null
        private var longitude: Double? = null

        init {
            itemView.setOnClickListener {
                chooseCityCallback(
                    latitude ?: TOKYO_NERIMA_LATITUDE_DEFAULT,
                    longitude ?: TOKYO_NERIMA_LONGITUDE_DEFAULT
                )
            }
        }

        fun bind(searchCityItemUI: SearchCityItemUI) {
            latitude = searchCityItemUI.lat
            longitude = searchCityItemUI.lon
            itemSearchCityBinding.city.text = searchCityItemUI.name
                .plus(", ${searchCityItemUI.country}")
                .plus(", ${searchCityItemUI.state.orEmpty()}")
                .dropLastWhile { it == ' ' || it == ',' }
        }
    }

    class SearchCityDiffUtilCallback : DiffUtil.ItemCallback<SearchCityItemUI>() {
        override fun areItemsTheSame(
            oldItem: SearchCityItemUI,
            newItem: SearchCityItemUI
        ): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
        }

        override fun areContentsTheSame(
            oldItem: SearchCityItemUI,
            newItem: SearchCityItemUI
        ): Boolean {
            return oldItem == newItem
        }

    }
}
