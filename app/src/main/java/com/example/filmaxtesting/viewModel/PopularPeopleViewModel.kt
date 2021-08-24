package com.example.filmaxtesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.pagingSource.PopularPeoplePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel
@Inject
constructor(
    val apiService: ApiService
) : ViewModel() {

    val popularPeopleList = Pager(PagingConfig(1)) {
        PopularPeoplePagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

}