package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class GetPeopleMoviesResponse(
    @SerializedName("id") val idPeople:Long,
    @SerializedName("cast")  val castCredits:List<Movie>,
    @SerializedName("crew")  val crewCredits:List<Movie>
)
