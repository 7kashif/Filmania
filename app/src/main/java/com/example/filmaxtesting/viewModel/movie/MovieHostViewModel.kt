package com.example.filmaxtesting.viewModel.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmaxtesting.apiService.RetrofitInstance
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import com.example.filmaxtesting.dataClasses.movies.upComingMovies.UpComingMoviesResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class MovieHostViewModel : ViewModel() {

    private val _upComingMovies = MutableLiveData<Response<UpComingMoviesResponse>>()
    private val _trendingMovies = MutableLiveData<Response<MoviesResponse>>()
    private val _popularMovies = MutableLiveData<Response<MoviesResponse>>()
    private val _topRatedMovies = MutableLiveData<Response<MoviesResponse>>()

    val upComingMovies: LiveData<Response<UpComingMoviesResponse>> get() = _upComingMovies
    val trendingMovies: LiveData<Response<MoviesResponse>> get() = _trendingMovies
    val popularMovies: LiveData<Response<MoviesResponse>> get() = _popularMovies
    val topRatedMovies: LiveData<Response<MoviesResponse>> get() = _topRatedMovies

    init {
        getUpcomingMovies()
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch {
            _upComingMovies.value = try{
                RetrofitInstance.api.getUpcomingMovies(page = 1)
            } catch (e:HttpException) {
                Log.e("httpException","internet problem")
                return@launch
            }
            getTrendingMovies()
        }
    }

    private fun getTrendingMovies() {
        viewModelScope.launch {
            _trendingMovies.value = try{
                RetrofitInstance.api.getTrendingMovies()
            } catch (e:HttpException) {
                Log.e("httpException","internet problem")
                return@launch
            }
            getPopularMovies()
        }
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            _popularMovies.value = try{
                RetrofitInstance.api.getPopularMovies(page = 1)
            } catch (e:HttpException) {
                Log.e("httpException","internet problem")
                return@launch
            }
            getTopRatedMovies()
        }
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            _topRatedMovies.value = try{
                RetrofitInstance.api.getTopRatedMovies(page = 1)
            } catch (e:HttpException) {
                Log.e("httpException","internet problem")
                return@launch
            }
        }
    }

}