package com.adamoi.coronannya.models
import com.google.gson.annotations.SerializedName


data class Countries(
    @SerializedName("countries")
    val countries: List<Country>
)

data class Country(
    @SerializedName("iso2")
    val iso2: String,
    @SerializedName("iso3")
    val iso3: String,
    @SerializedName("name")
    val name: String
)