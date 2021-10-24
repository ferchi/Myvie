package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class People (
    @SerializedName("id") val idPeople: Int,
    @SerializedName("name") val namePeople: String,
    @SerializedName("profile_path") val profilePath: String,
    @SerializedName("known_for") val known: String
    )