package com.example.weatherforecast.domain.model.geo

interface Coordinates {
    val lat: Double
    val lon: Double
    val name: String
    val country: String
    val state: String?
}
