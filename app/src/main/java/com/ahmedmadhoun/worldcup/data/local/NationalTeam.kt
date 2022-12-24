package com.ahmedmadhoun.worldcup.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "national_teams")
data class /**/NationalTeam(
    var name: String,
    var round: Int,
    var group: Int,
    var listType:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
): Parcelable
