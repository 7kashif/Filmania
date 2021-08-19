package com.example.filmaxtesting.viewModel.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieDetailsViewModelFactory(
    val id:Int
) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailsViewModel(id)::class.java))
            return MovieDetailsViewModel(id) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}