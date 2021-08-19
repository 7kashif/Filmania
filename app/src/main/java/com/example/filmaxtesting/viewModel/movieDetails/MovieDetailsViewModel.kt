package com.example.filmaxtesting.viewModel.movieDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmaxtesting.apiService.RetrofitInstance
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.dataClasses.relatedImages.RelatedImagesResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MovieDetailsViewModel(val id: Int) : ViewModel() {

    private val _movieDetails = MutableLiveData<Response<MovieDetailsResponse>>()
    val movieDetail: LiveData<Response<MovieDetailsResponse>> get() = _movieDetails

    private val _posters = MutableLiveData<Response<RelatedImagesResponse>>()
    val posters: LiveData<Response<RelatedImagesResponse>> get() = _posters

    private val _credits=MutableLiveData<Response<CreditsResponse>>()
    val credits:LiveData<Response<CreditsResponse>> get()=_credits

    init {
        getMovieDetail(id)
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.value = try {
                RetrofitInstance.api.getMovieDetails(movie_id = movieId)
            } catch (e: Exception) {
                Log.e("sharedViewModel", "An Error Occurred with movie details")
                return@launch
            }
        }
    }

    fun getPosters(movieId: Int) {
        viewModelScope.launch {
            _posters.value = try {
                RetrofitInstance.api.getRelatedImages(movieId = movieId)
            } catch (e:IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e:HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }

        }
    }

    fun getCredits(movieId: Int) {
        viewModelScope.launch {
            _credits.value = try {
                RetrofitInstance.api.getCredits(movieId = movieId)
            } catch (e:IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e:HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }

        }
    }

}