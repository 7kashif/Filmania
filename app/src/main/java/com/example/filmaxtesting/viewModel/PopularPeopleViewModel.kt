package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.popularPeople.Person
import com.example.filmaxtesting.pagingSource.PopularPeoplePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel
@Inject
constructor(
    val apiService: ApiService
) : ViewModel() {

    val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO+viewModelJob)
    var peopleList : Flow<PagingData<Person>>?=null

    fun getPopularPeople() {
        uiScope.launch {
            peopleList = beginLoading()
        }
    }

    private suspend fun beginLoading(): Flow<PagingData<Person>> {
        return withContext(Dispatchers.IO) {
            Pager(PagingConfig(1)) {
                PopularPeoplePagingSource(apiService)
            }.flow.cachedIn(viewModelScope)

        }
    }

}