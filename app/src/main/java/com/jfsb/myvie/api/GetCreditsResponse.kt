package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class GetCreditsResponse(
    @SerializedName("id") val idMovie:Long,
    @SerializedName("cast")  val casts:List<Cast>,
    @SerializedName("crew")  val crews:List<Crew>
)
