package com.jfsb.myvie.main.collection

import com.jfsb.myvie.api.Movie
import com.google.gson.annotations.SerializedName


data class Collection (
    @SerializedName("id") val collectionId:String = "",
    @SerializedName("author") val authorId:String = "",
    @SerializedName("name") val collectionName:String = "",
    @SerializedName("movies") val collectionMovies:List<Long>? = null,
    @SerializedName("image") val collectionImage:String? = "",
    @SerializedName("date") val collectionDate:String = ""
    )