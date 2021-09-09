package com.example.filmaxtesting.viewModel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.movies.Movies
import com.example.filmaxtesting.pagingSource.movie.TrendingMoviesPagingSource
import com.example.filmaxtesting.pagingSource.movie.UpComingMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class UpComingMoviesViewModel
@Inject
constructor(
    private val apiService: ApiService
):ViewModel(){

    val viewModelJOb = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJOb)
    var moviesList : Flow<PagingData<Movies>>? = null

    fun getUpcomingMovies() {
       uiScope.launch {
           moviesList = beginLoading()
       }
    }

    private suspend fun beginLoading(): Flow<PagingData<Movies>> {
        return withContext(Dispatchers.IO) {
            Pager(PagingConfig(pageSize = 1)) {
                UpComingMoviesPagingSource(apiService)
            }.flow.cachedIn(viewModelScope)
        }
    }
}