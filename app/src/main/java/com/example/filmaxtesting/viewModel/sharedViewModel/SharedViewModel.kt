package com.example.filmaxtesting.viewModel.sharedViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.filmaxtesting.roomDatabase.BookMark
import com.example.filmaxtesting.roomDatabase.BookMarkDatabaseDao
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
            val bookMark=BookMark()
            bookMark.apply {
                title = item.title
                voteAverage = item.voteAverage
                language = item.language
                releaseDate = item.releaseDate
                overView = item.overView
                posterPath = item.posterPath
                backDropPath=item.backDropPath
            }
            insertInDatabase(bookMark)
        }
    }


    fun onClear() {
        uiScope.launch {
            clearDatabase()
        }
    }

//    fun getBookMarkWithId(id:Long): MediatorLiveData<BookMark> {
//        val item=MediatorLiveData<BookMark>()
//        item.addSource(dataBase.getBookMarkById(id),item::setValue)
//        return item
//    }

    fun getBookMarkWithTitle(title: String){
        uiScope.launch {
            val bookMark=getBookMarkWithTitleFromDb(title)
            bookMark?.let {
                deleteSingleBookMarkFromDatabase(bookMark.bookMarkId)
            }
        }
    }

    private suspend fun getBookMarkWithTitleFromDb(title:String):BookMark? {
        return withContext(Dispatchers.IO) {
            dataBase.getBookMarkByTitle(title)
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