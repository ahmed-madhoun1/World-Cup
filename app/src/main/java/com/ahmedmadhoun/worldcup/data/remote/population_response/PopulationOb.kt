package com.ahmedmadhoun.worldcup.data.remote.population_response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "population")
data class PopulationOb(

    @SerializedName("ID Nation") var IDNation: String? = null,
    @SerializedName("Nation") var Nation: String? = null,
    @SerializedName("ID Year") var IDYear: Int? = null,
    @SerializedName("Year") var Year: String? = null,
    @SerializedName("Population") var Population: Int? = null,
    @SerializedName("Slug Nation") var SlugNation: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) : Parcelable