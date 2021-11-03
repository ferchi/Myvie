package com.jfsb.myvie.main.collection

import com.jfsb.myvie.api.Movie
import com.google.gson.annotations.SerializedName
import java.util.*


data class Collection (
   val id:String? = null,
   val author:String? = null,
   val name:String? = null,
   val movies:List<String>? = arrayListOf(),
   val image:String? = null,
   val date: String? = null
    )