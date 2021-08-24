package com.example.filmaxtesting.viewModel.personDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmaxtesting.apiService.RetrofitInstance
import com.example.filmaxtesting.dataClasses.personDetails.PersonDetailsResponse
import com.example.filmaxtesting.dataClasses.personDetails.PersonRelatedImagesResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class PersonDetailsViewModel(private val personId:Int) : ViewModel() {

    private val _personDetails = MutableLiveData<Response<PersonDetailsResponse>> ()
    val personDetails : LiveData<Response<PersonDetailsResponse>> get() = _personDetails

    private val _images = MutableLiveData <Response<PersonRelatedImagesResponse>> ()
    val images : LiveData<Response<PersonRelatedImagesResponse>> get() = _images

    init {
        getPersonDetails()
    }

    private fun getPersonDetails() {
        viewModelScope.launch {
            _personDetails.value=try {
                RetrofitInstance.api.getPersonDetails(person_id = personId)
            } catch (e:Exception) {
                Log.e("personDetailsViewModel","an error occurred")
                return@launch
            }
        }
    }

    fun getImages() {
        viewModelScope.launch {
            _images.value=try {
                RetrofitInstance.api.getPersonRelatedImages(person_id = personId)
            } catch (e:Exception) {
                Log.e("personDetailsViewModel","an error occurred")
                return@launch
            }
        }
    }

}