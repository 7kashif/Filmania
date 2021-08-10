package com.example.filmaxtesting.apiService

import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import com.example.filmaxtesting.dataClasses.tvShows.TvShowsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Constants.END_POPULAR_MOVIES_URL)
    suspend fun getPopularMovies(
        @Query("api_key") api_key:String= Constants.API_KEY,
        @Query("page") page:Int
    ):Response<MoviesResponse>

    @GET(Constants.END_TOP_RATED_MOVIES_URL)
    suspend fun getTopRatedMovies(
        @Query("api_key")api_key:String=Constants.API_KEY,
        @Query("page")page:Int
    ):Response<MoviesResponse>

    @GET(Constants.END_POPULAR_SHOWS_URL)
    suspend fun getPopularShows(
        @Query("api_key")api_key:String= Constants.API_KEY,
        @Query("page")page:Int
    ):Response<TvShowsResponse>

    @GET(Constants.END_TOP_RATED_SHOWS_URL)
    suspend fun getTopRatedShows(
        @Query("api_key")api_key:String= Constants.API_KEY,
        @Query("page")page:Int
    ):Response<TvShowsResponse>

    @GET(Constants.END_SEARCH_MOVIES_URL)
    suspend fun searchMovies(
        @Query("api_key") api_key:String=Constants.API_KEY,
        @Query("query") query:String
    ):Response<MoviesResponse>
}