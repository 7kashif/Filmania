package com.example.filmaxtesting.viewModel.showDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmaxtesting.apiService.RetrofitInstance
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.relatedImages.RelatedImagesResponse
import com.example.filmaxtesting.dataClasses.showsDetails.ShowDetailsResponse
import com.example.filmaxtesting.dataClasses.tvShows.TvShowsResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ShowDetailsViewModel(private val showId:Int): ViewModel() {

    private val _showDetails=MutableLiveData<Response<ShowDetailsResponse>> ()
    val showDetails : LiveData<Response<ShowDetailsResponse>> get() = _showDetails

    private val _posters = MutableLiveData<Response<RelatedImagesResponse>>()
    val posters: LiveData<Response<RelatedImagesResponse>> get() = _posters

    private val _credits = MutableLiveData<Response<CreditsResponse>> ()
    val credits : LiveData<Response<CreditsResponse>> get() = _credits

    private val _similarShows = MutableLiveData<Response<TvShowsResponse>> ()
    val similarShows: LiveData<Response<TvShowsResponse>> get() = _similarShows

    init {
        getShowDetail()
    }

    private fun getShowDetail() {
        viewModelScope.launch {
            _showDetails.value = try {
                RetrofitInstance.api.getShowDetails(showId = showId)
            } catch (e: Exception) {
                Log.e("sharedViewModel", "An Error Occurred with movie details")
                return@launch
            }
        }
    }

    fun getPosters() {
        viewModelScope.launch {
            _posters.value = try {
                RetrofitInstance.api.getShowRelatedImages(showId = showId)
            } catch (e: IOException) {
                Log.e("sharedViewModel", "IOException, connection problem.")
                return@launch
            } catch (e: HttpException) {
                Log.e("sharedViewModel", "HttpException, unexpected response.")
                return@launch
            }

        }
    }

    fun getShowCredits() {
        viewModelScope.launch {
            _credits.value = try {
                RetrofitInstance.api.getShowCredits(showId = showId)
            } catch (e: Exception) {
                Log.e("sharedViewModel", "An Error Occurred with movie details")
                return@launch
            }
        }
    }

    fun getSimilarShows() {
        viewModelScope.launch {
            _similarShows.value = try {
                RetrofitInstance.api.getSimilarShows(showId = showId)
            } catch (e: Exception) {
                Log.e("sharedViewModel", "An Error Occurred with movie details")
                return@launch
            }
        }
    }



}