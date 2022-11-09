package com.example.weatherforecast.presentation.weather_forecast.weather.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentCityForecastWeatherBinding
import com.example.weatherforecast.presentation.weather_forecast.NeedRationaleDialog
import com.example.weatherforecast.presentation.weather_forecast.weather.event.WeatherForecastEvent
import com.example.weatherforecast.presentation.weather_forecast.search_city.fragment.SearchCityFragment
import com.example.weatherforecast.presentation.weather_forecast.weather.WeatherViewState
import com.example.weatherforecast.presentation.weather_forecast.weather.adapter.DailyForecastWeatherAdapter
import com.example.weatherforecast.presentation.weather_forecast.weather.adapter.HourlyForecastWeatherAdapter
import com.example.weatherforecast.presentation.weather_forecast.weather.view_model.CityForecastWeatherViewModel
import com.example.weatherforecast.utils.appComponent
import com.example.weatherforecast.utils.withArguments
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityForecastWeatherFragment : Fragment(R.layout.fragment_city_forecast_weather) {
    private val viewBinding by viewBinding(FragmentCityForecastWeatherBinding::bind)

    @Inject
    lateinit var cityForecastWeatherViewModelFactory: dagger.Lazy<CityForecastWeatherViewModel.Factory>

    private val viewModel: CityForecastWeatherViewModel by viewModels {
        cityForecastWeatherViewModelFactory.get()
    }

    private var dailyForecastWeatherAdapter: DailyForecastWeatherAdapter? = null
    private var hourlyForecastWeatherAdapter: HourlyForecastWeatherAdapter? = null

    private val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isAccessCoarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            val isAccessFineLocationGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            when {
                isAccessCoarseLocationGranted && isAccessFineLocationGranted -> {
                    viewModel.obtainEvent(WeatherForecastEvent.WeatherByLocationFetch())
                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ->
                    showRationaleDialog()

                else -> {
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.impossible_get_current_location_without_permission),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        render()
        val isSearchResultScreen = arguments?.getBoolean(IS_SEARCH_RESULT_SCREEN) ?: false
        if (isSearchResultScreen) {
            val latitude = requireArguments().getDouble(LATITUDE)
            val longitude = requireArguments().getDouble(LONGITUDE)
            viewModel.obtainEvent(
                WeatherForecastEvent.WeatherByCoordinatesFetch(
                    latitude,
                    longitude
                )
            )
        } else {
            viewModel.obtainEvent(
                WeatherForecastEvent.WeatherInitEvent(
                    isPermissionGranted(
                        permissions
                    )
                )
            )
            initMenu()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dailyForecastWeatherAdapter = null
        hourlyForecastWeatherAdapter = null
    }

    private fun initRecyclerView() {
        dailyForecastWeatherAdapter = DailyForecastWeatherAdapter()
        hourlyForecastWeatherAdapter = HourlyForecastWeatherAdapter()
        with(viewBinding.dailyWeatherRecyclerView) {
            adapter = dailyForecastWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        with(viewBinding.currentForecastWeatherItem.hourlyWeatherRecyclerView) {
            adapter = hourlyForecastWeatherAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.icon_search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.icon_search -> {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, SearchCityFragment())
                            .addToBackStack(null).commit()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.forecastWeather.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { weatherViewState ->
                when (weatherViewState) {
                    is WeatherViewState.Success -> {
                        if (weatherViewState.dataUI.isAppFirstStart) {
                            requestLocationPermission()
                            viewModel.obtainEvent(
                                WeatherForecastEvent.WeatherIsAppFirstStartEvent(
                                    false
                                )
                            )
                        }
                        setData(weatherViewState)
                        viewVisibility(weatherViewState)
                    }
                    is WeatherViewState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            weatherViewState.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        viewVisibility(weatherViewState)
                    }
                    is WeatherViewState.Loading -> viewVisibility(weatherViewState)
                    is WeatherViewState.Empty -> viewVisibility(weatherViewState)
                }
            }
        }
    }

    private fun setData(weatherViewState: WeatherViewState.Success) {
        dailyForecastWeatherAdapter?.updateForecastWeatherList(weatherViewState.dataUI.dailyDailyForecast)
        hourlyForecastWeatherAdapter?.updateForecastWeatherList(weatherViewState.dataUI.hourlyDailyForecast)
        with(viewBinding.currentForecastWeatherItem) {
            locationName.text = weatherViewState.dataUI.locationName
            weatherTemperature.text =
                getString(R.string.temperature, weatherViewState.dataUI.degrees)
            weatherDescription.text =
                getString(R.string.description, weatherViewState.dataUI.description)
            weatherFeelsLike.text =
                getString(R.string.feels_like, weatherViewState.dataUI.feelsLike)
            weatherWind.text = getString(R.string.wind_speed, weatherViewState.dataUI.wind)
            weatherPressure.text = getString(R.string.pressure, weatherViewState.dataUI.pressure)
            weatherHumidity.text = getString(R.string.humidity, weatherViewState.dataUI.humidity)

            Glide.with(requireContext())
                .load(getString(R.string.icon_url_4x, weatherViewState.dataUI.icon))
                .error(R.drawable.ic_download_error)
                .placeholder(R.drawable.ic_downloading).into(weatherIcon)
        }
    }

    private fun viewVisibility(weatherViewState: WeatherViewState) {
        with(viewBinding) {
            dailyWeatherRecyclerView.isVisible = weatherViewState is WeatherViewState.Success
            progressBar.isVisible = weatherViewState is WeatherViewState.Loading
            allViewGroup.isVisible = weatherViewState is WeatherViewState.Success
        }
    }

    private fun showRationaleDialog() {
        NeedRationaleDialog.newInstance(
            R.string.allow,
            R.string.reject,
            R.string.need_location_info_message
        ).show(childFragmentManager, NEED_RATIONALE_DIALOG)
    }

    fun requestLocationPermission() {
        locationPermission.launch(permissions.toTypedArray())
    }


    private fun isPermissionGranted(permissions: List<String>): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val NEED_RATIONALE_DIALOG = "needRationaleDialog"
        private const val LATITUDE = "LATITUDE"
        private const val LONGITUDE = "LONGITUDE"
        private const val IS_SEARCH_RESULT_SCREEN = "IS_SEARCH_RESULT_SCREEN"

        fun newInstance(
            lat: Double,
            lon: Double,
            isSearchResultScreen: Boolean = true
        ): CityForecastWeatherFragment {
            return CityForecastWeatherFragment().withArguments {
                putDouble(LATITUDE, lat)
                putDouble(LONGITUDE, lon)
                putBoolean(IS_SEARCH_RESULT_SCREEN, isSearchResultScreen)
            }
        }

    }
}
