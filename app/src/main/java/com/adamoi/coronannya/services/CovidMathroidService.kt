package com.adamoi.coronannya.services

import com.adamoi.coronannya.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidMathroidService {
    @GET("api/")
    fun getGlobal(): Call<GlobalSummary>

    @GET("api/countries/{country}")
    fun getCountry(@Path("country") country: String?): Call<CountrySummary>

    @GET("api/countries")
    fun getCountries(): Call<Countries>

    @GET("api/provinsi")
    fun getProvinsi(): Call<IndonesiaSummaryData>
}