package com.adamoi.coronannya.models
import com.google.gson.annotations.SerializedName

data class Confirmed(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("value")
    val value: Int
)

data class Deaths(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("value")
    val value: Int
)

data class Recovered(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("value")
    val value: Int
)
