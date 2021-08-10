package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.pagingSource.movie.PopularMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PopularMoviesViewModel
@Inject
constructor(
    val apiService: ApiService
): ViewModel(){

    val popularMoviesList= Pager(PagingConfig(pageSize = 1)) {
        PopularMoviesPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

}