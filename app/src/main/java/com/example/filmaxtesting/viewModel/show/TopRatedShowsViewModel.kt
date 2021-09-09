package com.example.filmaxtesting.viewModel.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.tvShows.TvShows
import com.example.filmaxtesting.pagingSource.shows.PopularShowsPagingSource
import com.example.filmaxtesting.pagingSource.shows.TopRatedShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class TopRatedShowsViewModel
@Inject
constructor(
     val apiService: ApiService
):ViewModel(){
    val viewModelJOb = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJOb)
    var showsList : Flow<PagingData<TvShows>>? = null

    fun getShows() {
        uiScope.launch {
            showsList = beginLoading()
        }
    }

    private suspend fun beginLoading(): Flow<PagingData<TvShows>> {
        return withContext(Dispatchers.IO) {
            Pager(PagingConfig(pageSize = 1)) {
                TopRatedShowsPagingSource(apiService)
            }.flow.cachedIn(viewModelScope)
        }
    }

}