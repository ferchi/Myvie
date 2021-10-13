package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class GetMoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<MovieResponse>,
    @SerializedName("total_pages") val pages: Int
)
