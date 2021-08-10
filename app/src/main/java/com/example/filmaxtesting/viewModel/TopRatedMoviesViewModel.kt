package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.pagingSource.movie.TopRatedMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TopRatedMoviesViewModel
@Inject
constructor(
    val apiService: ApiService
): ViewModel(){

    val topRatedMoviesList= Pager(PagingConfig(pageSize = 1)) {
        TopRatedMoviesPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)


}