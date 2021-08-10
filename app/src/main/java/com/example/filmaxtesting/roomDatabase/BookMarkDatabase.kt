package com.example.filmaxtesting.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookMark::class],version = 8,exportSchema = false)
abstract class BookMarkDatabase: RoomDatabase() {

    abstract val bookMarkDatabaseDao:BookMarkDatabaseDao

    companion object{
        private var INSTANCE:BookMarkDatabase?=null
        fun getInstance(context: Context):BookMarkDatabase{
            var instance= INSTANCE
            if(instance==null){
                instance= Room.databaseBuilder(
                    context.applicationContext,
                    BookMarkDatabase::class.java,
                    "bookmarks_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE=instance
            }
            return instance
        }
    }

}
