package com.example.filmaxtesting.viewModel.sharedViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.filmaxtesting.roomDatabase.BookMark
import com.example.filmaxtesting.roomDatabase.BookMarkDatabaseDao
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.*


class SharedViewModel(
    val dataBase:BookMarkDatabaseDao,
    application:Application
): AndroidViewModel(application) {

    private val viewModelJob= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main+viewModelJob)
    val allBookMarks=dataBase.getAllBookMarks()



    fun createBookMark(item: BookMark) {
        uiScope.launch {
            insertInDatabase(item)
        }
    }

    fun onClear() {
        uiScope.launch {
            clearDatabase()
        }
    }

    suspend fun getBookMarkByItemId(id:Int) : BookMark? {
        return withContext(Dispatchers.IO) {
            dataBase.getBookMarkByItemId(id)
        }
    }

    fun deleteSingleTask(id:Int) {
        uiScope.launch {
            val bookMark=getBookMarkByItemId(id)
            if (bookMark != null) {
                deleteSingleBookMarkFromDatabase(bookMark.bookMarkId)
            }
        }
    }

    private suspend fun deleteSingleBookMarkFromDatabase(id:Long) {
        withContext(Dispatchers.IO) {
            dataBase.deleteSingleBookMark(id)
        }
    }

    private suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            dataBase.clearAll()
        }
    }

    private suspend fun insertInDatabase(bookMark:BookMark) {
        withContext(Dispatchers.IO) {
            dataBase.insert(bookMark)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}