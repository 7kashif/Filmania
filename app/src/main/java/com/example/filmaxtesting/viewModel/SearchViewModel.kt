package com.example.filmaxtesting.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmaxtesting.apiService.RetrofitInstance
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import kotlinx.coroutines.launch
import retrofit2.Response


class SearchViewModel :ViewModel() {

    private val _moviesResponse=MutableLiveData<Response<MoviesResponse>>()
    val moviesResponse:LiveData<Response<MoviesResponse>> get() = _moviesResponse

//    var queryChannel:MutableStateFlow<String?>?= MutableStateFlow(null)
//    fun callQueryChannel() {
//        queryChannel?.debounce(500L)
//            ?.onEach {
//                searchMovies(it!!)
//            }
//            ?.launchIn(viewModelScope)
//    }



    fun searchMovies(title:String) {
      if(title.isNotEmpty()){
          viewModelScope.launch {
              _moviesResponse.value= try {
                  RetrofitInstance.api.searchMovies(query = title)
              } catch (e:Exception) {
                  Log.e("Search","Some error happened.")
                  return@launch
              }
          }
      }
    }

}