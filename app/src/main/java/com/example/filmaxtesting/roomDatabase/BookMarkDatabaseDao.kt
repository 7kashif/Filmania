package com.example.filmaxtesting.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface BookMarkDatabaseDao {
    @Insert
    fun insert(bookMark:BookMark)

    @Update
    fun update(bookMark:BookMark)

    @Query("DELETE FROM bookmark")
    fun clearAll()

    @Query("SELECT * FROM bookmark ORDER BY bookMarkId DESC")
    fun getAllBookMarks(): LiveData<List<BookMark>>

    @Query("SELECT * FROM bookmark ORDER BY bookMarkId DESC LIMIT 1")
    fun getLastBookMark():BookMark?

    @Query("SELECT * FROM bookmark WHERE itemId=:id")
    fun getBookMarkByItemId(id:Int):BookMark?

    @Query("DELETE  FROM bookmark WHERE bookMarkId=:key")
    fun deleteSingleBookMark(key:Long)
}