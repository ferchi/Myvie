package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class Cast (
    @SerializedName("id") val idCast: Long,
    @SerializedName("name") val nameCast: String,
    @SerializedName("profile_path") val profilePath: String,
    @SerializedName("character") val character: String,
    @SerializedName("known_for_department") val department: String
        )