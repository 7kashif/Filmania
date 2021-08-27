package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.movies.Movies
import com.example.filmaxtesting.databinding.FragmentPopularMoviesBinding
import com.example.filmaxtesting.pagingSource.movie.PopularMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class PopularMoviesViewModel
@Inject
constructor(
    val apiService: ApiService
): ViewModel(){

    var moviesList: Flow<PagingData<Movies>>?=null

    fun getMovies(binding : FragmentPopularMoviesBinding) {
            moviesList=beginLoading(binding)
    }

    private fun beginLoading(binding: FragmentPopularMoviesBinding):Flow<PagingData<Movies>> {
        return Pager(PagingConfig(pageSize = 1)) {
            PopularMoviesPagingSource(apiService,binding)
        }.flow.cachedIn(viewModelScope)
    }
}