package com.example.filmaxtesting.pagingSource.shows

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.tvShows.TvShows
import java.util.*

class PopularShowsPagingSource(private val apiService: ApiService): PagingSource<Int, TvShows>() {
    override fun getRefreshKey(state: PagingState<Int, TvShows>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShows> {
        return  try {

            val currentPage=params.key?:1
            val response=apiService.getPopularShows(page=currentPage)
            val responseData= mutableListOf<TvShows>()
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