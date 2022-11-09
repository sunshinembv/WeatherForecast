package com.example.weatherforecast.presentation.weather_forecast.search_city.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.usecases.GetCoordinatesUseCase
import com.example.weatherforecast.presentation.weather_forecast.search_city.SearchCityViewState
import com.example.weatherforecast.presentation.weather_forecast.search_city.mapper.SearchCityMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchCityViewModel(
    private val getCoordinatesUseCase: GetCoordinatesUseCase,
    private val searchCityMapper: SearchCityMapper
) : ViewModel() {

    private val _searchCity = MutableStateFlow<SearchCityViewState>(SearchCityViewState.Empty)
    val searchCity: StateFlow<SearchCityViewState> = _searchCity.asStateFlow()


    private fun getCoordinatesByLocationName(locationName: String, limit: Int = 5) {
        viewModelScope.launch {
            try {
                val coordinates = getCoordinatesUseCase.execute(locationName, limit)
                val searchCityItemUI = searchCityMapper.fromCoordinates(coordinates)
                _searchCity.emit(SearchCityViewState.Success(searchCityItemUI))
            } catch (t: Throwable) {
                Log.d(TAG_ERROR, t.toString())
                _searchCity.emit(SearchCityViewState.Error(t.toString()))
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun searchCity(queryFlow: Flow<String>) {
        queryFlow.filter { it.isNotEmpty() }.debounce(500).mapLatest { locationName ->
            getCoordinatesByLocationName(locationName)
        }.launchIn(viewModelScope)
    }

    class Factory @Inject constructor(
        private val getCoordinatesUseCase: GetCoordinatesUseCase,
        private val searchCityMapper: SearchCityMapper
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SearchCityViewModel::class.java)
            return SearchCityViewModel(
                getCoordinatesUseCase,
                searchCityMapper
            ) as T
        }
    }

    companion object {
        private val TAG_ERROR = SearchCityViewModel::class.java.simpleName
    }
}