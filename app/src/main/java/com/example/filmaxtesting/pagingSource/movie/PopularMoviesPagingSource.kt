package com.example.filmaxtesting.pagingSource.movie

import android.view.View
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.movies.Movies
import com.example.filmaxtesting.databinding.FragmentPopularMoviesBinding
import java.util.Collections.emptyList


class PopularMoviesPagingSource(
    private val apiService: ApiService,
    private val binding: FragmentPopularMoviesBinding
) : PagingSource<Int, Movies>() {
    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {

            val currentPage = params.key ?: 1
            val response = apiService.getPopularMovies(page = currentPage)
            if (response.isSuccessful && response.body() != null) {
                binding.apply {
                    binding.linearLayout.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                }
            }
            val responseData = mutableListOf<Movies>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}