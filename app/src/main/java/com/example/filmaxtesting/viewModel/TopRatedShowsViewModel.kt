package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.pagingSource.shows.TopRatedShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TopRatedShowsViewModel
@Inject
constructor(
     val apiService: ApiService
):ViewModel(){

    val topRatedShowsList= Pager(PagingConfig(pageSize = 1)) {
        TopRatedShowsPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

}