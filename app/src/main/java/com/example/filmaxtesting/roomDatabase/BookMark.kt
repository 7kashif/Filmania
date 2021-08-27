package com.example.filmaxtesting.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="bookmark")
data class BookMark(
    @PrimaryKey(autoGenerate = true)
    var bookMarkId:Long=0L,
    @ColumnInfo(name="itemId") //id of movie or show  provided by TMDB
    var itemId:Int=0,
    @ColumnInfo(name = "title")
    var title:String="Title",
    @ColumnInfo(name = "poster_path")
    var posterPath:String?=null,
    @ColumnInfo(name = "vote_average")
    var voteAverage:Double = 0.0,
    @ColumnInfo(name = "media_type")
    var mediaType:String = ""
)
