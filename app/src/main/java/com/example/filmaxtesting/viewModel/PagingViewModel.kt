package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.pagingSource.movie.PopularMoviesPagingSource
import com.example.filmaxtesting.pagingSource.movie.TopRatedMoviesPagingSource
import com.example.filmaxtesting.pagingSource.shows.PopularShowsPagingSource
import com.example.filmaxtesting.pagingSource.shows.TopRatedShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PagingViewModel
@Inject
constructor(
    val apiService: ApiService
): ViewModel() {
    var lastFragment:String?=null


    val popularMoviesList= Pager(PagingConfig(pageSize = 1)) {
        PopularMoviesPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    val topRatedMoviesList= Pager(PagingConfig(pageSize = 1)) {
        TopRatedMoviesPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    val popularShowsList= Pager(PagingConfig(pageSize=1)){
        PopularShowsPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    val topRatedShowsList=Pager(PagingConfig(pageSize = 1)) {
        TopRatedShowsPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

}