package com.example.weatherforecast.presentation.weather_forecast

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.example.weatherforecast.R
import com.example.weatherforecast.presentation.weather_forecast.weather.fragment.CityForecastWeatherFragment
import com.example.weatherforecast.utils.withArguments

class NeedRationaleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).setMessage(
            getString(
                requireArguments().getInt(
                    DIALOG_LOCATION_INFO_MESSAGE
                )
            )
        ).setPositiveButton(getString(requireArguments().getInt(DIALOG_ALLOW))) { _, _ ->
            (parentFragment as? CityForecastWeatherFragment)?.requestLocationPermission()
        }
            .setNegativeButton(getString(requireArguments().getInt(DIALOG_REJECT))) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    getText(R.string.setting_location_permission_message),
                    Toast.LENGTH_LONG
                ).show()
            }
            .create()
    }

    companion object {
        private const val DIALOG_ALLOW = "dialogAllow"
        private const val DIALOG_REJECT = "dialogReject"
        private const val DIALOG_LOCATION_INFO_MESSAGE = "dialogLocationInfoMessage"

        fun newInstance(
            @StringRes allow: Int,
            @StringRes reject: Int,
            @StringRes message: Int
        ): NeedRationaleDialog {
            return NeedRationaleDialog().withArguments {
                putInt(DIALOG_ALLOW, allow)
                putInt(DIALOG_REJECT, reject)
                putInt(DIALOG_LOCATION_INFO_MESSAGE, message)
            }
        }
    }
}