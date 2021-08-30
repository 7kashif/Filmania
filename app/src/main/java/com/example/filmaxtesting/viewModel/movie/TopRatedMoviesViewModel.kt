package com.example.filmaxtesting.viewModel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.movies.Movies
import com.example.filmaxtesting.databinding.FragmentTopRatedMoviesBinding
import com.example.filmaxtesting.pagingSource.movie.TopRatedMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class TopRatedMoviesViewModel
@Inject
constructor(
    val apiService: ApiService
) : ViewModel() {
    val viewModelJob= Job()
    private val uiScope= CoroutineScope(Dispatchers.IO+viewModelJob)

    var moviesList: Flow<PagingData<Movies>>? = null

    fun getMovies(binding: FragmentTopRatedMoviesBinding) {
        moviesList = beginLoading(binding)
    }

    private fun beginLoading(binding: FragmentTopRatedMoviesBinding): Flow<PagingData<Movies>> {
        return Pager(PagingConfig(pageSize = 1)) {
            TopRatedMoviesPagingSource(apiService, binding)
        }.flow.cachedIn(viewModelScope)
    }

//    override fun onCleared() {
//        super.onCleared()
//        Log .e("topRatedViewModel","onCleared Called")
//        viewModelJob.cancel()
//    }
}