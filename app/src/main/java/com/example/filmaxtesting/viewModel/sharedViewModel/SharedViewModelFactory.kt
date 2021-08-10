package com.example.filmaxtesting.viewModel.sharedViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmaxtesting.roomDatabase.BookMarkDatabaseDao

class SharedViewModelFactory(
    private val dataBase:BookMarkDatabaseDao,
    private val application: Application
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SharedViewModel(dataBase,application)::class.java))
            return SharedViewModel(dataBase,application) as T
        throw IllegalArgumentException("unknown Model Class")
    }
}