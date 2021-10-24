package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val genreName: String
)
