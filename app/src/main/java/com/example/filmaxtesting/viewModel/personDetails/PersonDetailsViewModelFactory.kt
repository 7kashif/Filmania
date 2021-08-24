package com.example.filmaxtesting.viewModel.personDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PersonDetailsViewModelFactory(
    private val personId:Int
):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PersonDetailsViewModel(personId)::class.java))
            return PersonDetailsViewModel(personId) as T
        throw  IllegalArgumentException("Unknown model class")
    }
}