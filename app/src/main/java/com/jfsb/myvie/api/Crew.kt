package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("id") val idCrew: Long,
    @SerializedName("name") val nameCrew: String,
    @SerializedName("profile_path") val profilePath: String,
    @SerializedName("job") val jobCrew: String,
    @SerializedName("known_for_department") val departmentCrew: String
)
