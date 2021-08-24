package com.example.filmaxtesting.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResults
import java.util.*

class SearchPagingSource(
    private val apiService: ApiService,
    private val queryString: String
):PagingSource<Int,MultiSearchResults>() {

    override fun getRefreshKey(state: PagingState<Int, MultiSearchResults>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MultiSearchResults> {
        return  try {

            val currentPage=params.key?:1
            val response=apiService.multiSearch(query = queryString,page=currentPage)
            val responseData= mutableListOf<MultiSearchResults>()
            val data=response.body()?.results?: Collections.emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data=responseData,
                prevKey = if(currentPage==1) null else -1,
                nextKey =currentPage.plus(1)
            )

        }catch (e:Exception) {
            LoadResult.Error(e)
        }
    }
}