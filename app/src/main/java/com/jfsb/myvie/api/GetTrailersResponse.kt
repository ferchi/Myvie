package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class GetTrailersResponse (
    @SerializedName("results") val trailers:List<Trailer>
)