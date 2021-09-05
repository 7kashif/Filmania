package com.example.filmaxtesting.viewModel.movieDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmaxtesting.apiService.RetrofitInstance
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import com.example.filmaxtesting.dataClasses.relatedImages.RelatedImagesResponse
import com.example.filmaxtesting.dataClasses.video.VideosResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MovieDetailsViewModel(private val movieId: Int) : ViewModel() {

    private val _movieDetails = MutableLiveData<Response<MovieDetailsResponse>>()
    val movieDetail: LiveData<Response<MovieDetailsResponse>> get() = _movieDetails

    private val _posters = MutableLiveData<Response<RelatedImagesResponse>>()
    val posters: LiveData<Response<RelatedImagesResponse>> get() = _posters

    private val _credits = MutableLiveData<Response<CreditsResponse>>()
    val credits: LiveData<Response<CreditsResponse>> get() = _credits

    private val _similarMoviesList = MutableLiveData<Response<MoviesResponse>>()
    val similarMoviesList: LiveData<Response<MoviesResponse>> get() = _similarMoviesList

    private val _videosList = MutableLiveData<Response<VideosResponse>>()
    val videosList : LiveData<Response<VideosResponse>> get() = _videosList

    init {
        getMovieDetail(movieId)
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.value = try {
                RetrofitInstance.api.getMovieDetails(movie_id = movieId)
            } catch (e: Exception) {
                Log.e("sharedViewModel", "An Error Occurred with movie details")
                return@launch
            }
            getPosters()
        }
    }

    private fun getPosters() {
        viewModelScope.launch {
            _posters.value = try {
                RetrofitInstance.api.getMovieRelatedImages(movieId = movieId)
            } catch (e: IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e: HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }
            getVideos()
        }
    }

    private fun getVideos() {
        viewModelScope.launch {
            _videosList.value = try {
                RetrofitInstance.api.getMovieVideos(movie_id = movieId)
            } catch (e: IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e: HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }
            getCredits()
        }
    }

    private fun getCredits() {
        viewModelScope.launch {
            _credits.value = try {
                RetrofitInstance.api.getMovieCredits(movieId = movieId)
            } catch (e: IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e: HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }
            getSimilarMovies()
        }
    }

    private fun getSimilarMovies() {
        viewModelScope.launch {
            _similarMoviesList.value = try {
                RetrofitInstance.api.getSimilarMovies(movieId = movieId)
            } catch (e: IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e: HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }
        }
    }

}