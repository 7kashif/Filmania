package com.example.filmaxtesting.pagingSource

import android.util.Log
import android.view.View
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResponse
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResults
import com.example.filmaxtesting.databinding.FragmentSearchBinding
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.*

class SearchPagingSource(
    private val apiService: ApiService,
    private val queryString: String,
    private val binding : FragmentSearchBinding
):PagingSource<Int,MultiSearchResults>() {

    override fun getRefreshKey(state: PagingState<Int, MultiSearchResults>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MultiSearchResults> {
        return try {
            val currentPage=params.key?:1
            val response=apiService.multiSearch(query = queryString,page=currentPage)
            if(response.isSuccessful)
                binding.progressbar.visibility = View.GONE
            val responseData= mutableListOf<MultiSearchResults>()
            val data= response.body()?.results?: Collections.emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data=responseData,
                prevKey = if(currentPage==1) null else -1,
                nextKey =currentPage.plus(1)
            )
        }  catch (e:Exception) {
            LoadResult.Error(e)
        }

    }
}