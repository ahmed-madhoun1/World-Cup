package com.ahmedmadhoun.worldcup.data.remote.population_response

import com.google.gson.annotations.SerializedName


data class Source(

    @SerializedName("measures") var measures: ArrayList<String> = arrayListOf(),
    @SerializedName("annotations") var annotations: Annotations? = Annotations(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("substitutions") var substitutions: ArrayList<String> = arrayListOf()

)