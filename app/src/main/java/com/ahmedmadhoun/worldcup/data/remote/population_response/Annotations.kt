package com.ahmedmadhoun.worldcup.data.remote.population_response

import com.google.gson.annotations.SerializedName


data class Annotations(

    @SerializedName("source_name") var sourceName: String? = null,
    @SerializedName("source_description") var sourceDescription: String? = null,
    @SerializedName("dataset_name") var datasetName: String? = null,
    @SerializedName("dataset_link") var datasetLink: String? = null,
    @SerializedName("table_id") var tableId: String? = null,
    @SerializedName("topic") var topic: String? = null,
    @SerializedName("subtopic") var subtopic: String? = null

)