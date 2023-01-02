package com.ahmedmadhoun.worldcup.data.remote.pets_response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pet")
data class Pet(
    val API: String,
    val Auth: String,
    val Category: String,
    val Cors: String,
    val Description: String,
    val HTTPS: Boolean,
    val Link: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) : Parcelable