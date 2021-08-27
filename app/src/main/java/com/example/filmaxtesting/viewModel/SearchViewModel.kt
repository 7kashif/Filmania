package com.example.filmaxtesting.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResults
import com.example.filmaxtesting.databinding.FragmentSearchBinding
import com.example.filmaxtesting.pagingSource.SearchPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    val apiService: ApiService
):ViewModel() {
    var searchResults: Flow<PagingData<MultiSearchResults>>? =null

    fun search(query:String, binding: FragmentSearchBinding) {
        searchResults=beginSearch(query, binding)
    }

    private fun beginSearch(queryString: String, binding: FragmentSearchBinding): Flow<PagingData<MultiSearchResults>> {
        return Pager(PagingConfig(pageSize = 1)) {
            SearchPagingSource(apiService,queryString,binding)
        }.flow.cachedIn(viewModelScope)
    }

}