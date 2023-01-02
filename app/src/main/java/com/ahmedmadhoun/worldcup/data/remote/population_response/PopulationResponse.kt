package com.ahmedmadhoun.worldcup.data.remote.population_response

import com.google.gson.annotations.SerializedName


data class PopulationResponse(

    @SerializedName("data") var data: ArrayList<PopulationOb> = arrayListOf(),
    @SerializedName("source") var source: ArrayList<Source> = arrayListOf()

)