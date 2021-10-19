package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("id") val idTrailer: String,
    @SerializedName("key") val linkTrailer: String,
    @SerializedName("site") val siteTrailer: String
)
