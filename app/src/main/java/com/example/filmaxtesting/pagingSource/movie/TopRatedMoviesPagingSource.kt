package com.example.filmaxtesting.pagingSource.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.movies.Movies
import java.util.*

class TopRatedMoviesPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Movies>() {
    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {

            val currentPage = params.key ?: 1
            val response = apiService.getTopRatedMovies(page = currentPage)
            val responseData = mutableListOf<Movies>()
            val data = response.body()?.results ?: Collections.emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = if (currentPage.plus(1) == response.body()!!.total_pages)
                    null else currentPage.plus(1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}