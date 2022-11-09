package com.example.weatherforecast.presentation.weather_forecast.search_city.fragment

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSearchCityBinding
import com.example.weatherforecast.presentation.weather_forecast.search_city.SearchCityViewState
import com.example.weatherforecast.presentation.weather_forecast.search_city.adapter.SearchCityAdapter
import com.example.weatherforecast.presentation.weather_forecast.search_city.view_model.SearchCityViewModel
import com.example.weatherforecast.presentation.weather_forecast.weather.fragment.CityForecastWeatherFragment
import com.example.weatherforecast.utils.appComponent
import com.example.weatherforecast.utils.queryTextFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchCityFragment : Fragment(R.layout.fragment_search_city) {

    private val viewBinding by viewBinding(FragmentSearchCityBinding::bind)

    @Inject
    lateinit var searchCityViewModelFactory: dagger.Lazy<SearchCityViewModel.Factory>

    private val searchCityViewModel: SearchCityViewModel by viewModels {
        searchCityViewModelFactory.get()
    }

    private var searchCityAdapter: SearchCityAdapter? = null
    private var searchView: SearchView? = null

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()
        initRecyclerView()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchCityAdapter = null
        searchView = null
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem = menu.findItem(R.id.action_search)
                searchItem.expandActionView()
                searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(p0: MenuItem): Boolean = false

                    override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                        requireActivity().supportFragmentManager.popBackStack()
                        return true
                    }
                })
                searchView = searchItem.actionView as SearchView
                searchView?.let {
                    searchCityViewModel.searchCity(it.queryTextFlow())
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> true
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() {
        searchCityAdapter = SearchCityAdapter { latitude, longitude ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    CityForecastWeatherFragment.newInstance(latitude, longitude)
                ).addToBackStack(null)
                .commit()
        }
        with(viewBinding.searchCityRecyclerView) {
            adapter = searchCityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchCityViewModel.searchCity.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { searchCityViewState ->
                when (searchCityViewState) {
                    is SearchCityViewState.Success -> {
                        searchCityAdapter?.updateCityList(searchCityViewState.dataUI)
                        viewVisibility(searchCityViewState)
                    }
                    is SearchCityViewState.Error -> {
                        searchCityAdapter?.updateCityList(emptyList())
                        Toast.makeText(
                            requireContext(),
                            searchCityViewState.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        viewVisibility(searchCityViewState)
                    }
                    is SearchCityViewState.Loading -> viewVisibility(searchCityViewState)
                    is SearchCityViewState.Empty -> viewVisibility(searchCityViewState)
                }
            }
        }
    }

    private fun viewVisibility(searchCityViewState: SearchCityViewState) {
        viewBinding.infoMessage.isVisible = searchCityViewState is SearchCityViewState.Empty
        viewBinding.emptyMessage.isVisible =
            searchCityViewState is SearchCityViewState.Success && searchCityViewState.dataUI.isEmpty()
        viewBinding.searchCityRecyclerView.isVisible =
            searchCityViewState is SearchCityViewState.Success && searchCityViewState.dataUI.isNotEmpty()
    }

}
