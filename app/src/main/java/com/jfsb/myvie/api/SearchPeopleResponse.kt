package com.jfsb.myvie.api

import com.google.gson.annotations.SerializedName

data class SearchPeopleResponse(
    @SerializedName("page") val page:Int,
    @SerializedName("results")  val peopleList:List<People>,
    @SerializedName("total_pages") val pages: Int
)
