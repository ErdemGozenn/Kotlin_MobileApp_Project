package com.erdemgozen.hw1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityData(
    val cityName: String,
    val cityCost: Double

) : Parcelable{

    override fun toString(): String {
        return cityName + "\t->\t" + cityCost + "\n"
    }
}
