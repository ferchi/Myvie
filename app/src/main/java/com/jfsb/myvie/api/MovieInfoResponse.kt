package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class MovieInfoResponse @ExperimentalTime constructor(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genres") val genresList: List<Genre>,
    @SerializedName("runtime") val durationMovie: Long
)