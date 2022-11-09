package com.example.weatherforecast.utils

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.weatherforecast.MainApp
import com.example.weatherforecast.di.AppComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.*

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }

fun Long.toDateOrTime(patternStr: String): String {
    val simpleDateFormat = SimpleDateFormat(patternStr, Locale.ROOT)
    return simpleDateFormat.format(Date(this * 1000))
}

fun Int.pressureMbToMMhg(): Int {
    return (this / 1.333).toInt()
}

fun <T : Fragment> T.withArguments(args: Bundle.() -> Unit): T {
    return apply {
        arguments = Bundle().apply(args)
    }
}

fun SearchView.queryTextFlow(): Flow<String> {
    return callbackFlow {
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                trySend(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                trySend(newText.orEmpty())
                return true
            }
        }
        this@queryTextFlow.setOnQueryTextListener(queryTextListener)
        awaitClose {
            this@queryTextFlow.setOnQueryTextListener(null)
        }
    }
}
