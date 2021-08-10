package com.example.filmaxtesting.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="bookmark")
data class BookMark(
    @PrimaryKey(autoGenerate = true)
    var bookMarkId:Long=0L,
    @ColumnInfo(name="title")
    var title:String="title",
    @ColumnInfo(name = "vote_average")
    var voteAverage:Double=0.0,
    @ColumnInfo(name = "overview")
    var overView:String="OverView",
    @ColumnInfo(name="release_date")
    var releaseDate:String="yyyy-mm-dd",
    @ColumnInfo(name = "language")
    var language:String="-",
    @ColumnInfo(name = "poster_path")
    var posterPath:String="",
    @ColumnInfo(name = "backdrop_path")
    var backDropPath:String=""
)
