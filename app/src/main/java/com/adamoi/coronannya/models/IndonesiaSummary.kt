package com.adamoi.coronannya.models
import com.google.gson.annotations.SerializedName


data class IndonesiaSummaryData(
    @SerializedName("data")
    val `data`: List<IndonesiaSummary>
)

data class IndonesiaSummary(
    @SerializedName("fid")
    val fid: Int,
    @SerializedName("kasusMeni")
    val kasusMeni: Int,
    @SerializedName("kasusPosi")
    val kasusPosi: Int,
    @SerializedName("kasusSemb")
    val kasusSemb: Int,
    @SerializedName("kodeProvi")
    val kodeProvi: Int,
    @SerializedName("provinsi")
    val provinsi: String
)