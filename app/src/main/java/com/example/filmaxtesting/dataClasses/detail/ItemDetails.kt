package com.example.filmaxtesting.dataClasses.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemDetails(
    val title:String,
    val voteAverage:Double,
    val overView:String,
    val releaseDate:String,
    val language:String,
    val posterPath:String
):Parcelable
