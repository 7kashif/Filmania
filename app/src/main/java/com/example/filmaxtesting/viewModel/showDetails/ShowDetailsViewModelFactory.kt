package com.example.filmaxtesting.viewModel.showDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowDetailsViewModelFactory(
    private val showId:Int
):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailsViewModel(showId)::class.java))
            return ShowDetailsViewModel(showId) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}