package com.adamoi.coronannya.models

import com.google.gson.annotations.SerializedName

data class CountrySummary(
    @SerializedName("confirmed")
    val confirmed: Confirmed,
    @SerializedName("deaths")
    val deaths: Deaths,
    @SerializedName("lastUpdate")
    val lastUpdate: String,
    @SerializedName("recovered")
    val recovered: Recovered,
)

data class LocalSummary(
    @SerializedName("confirmed")
    val confirmed: String,
    @SerializedName("recovered")
    val recovered: String,
    @SerializedName("deaths")
    val deaths: String,
    @SerializedName("active")
    val active: String,
    @SerializedName("lastUpdate")
    val lastUpdate: String,
    @SerializedName("country")
    val country: String
)

