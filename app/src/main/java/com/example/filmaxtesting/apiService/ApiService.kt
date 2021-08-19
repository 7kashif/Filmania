package com.example.filmaxtesting.apiService

import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import com.example.filmaxtesting.dataClasses.relatedImages.RelatedImagesResponse
import com.example.filmaxtesting.dataClasses.tvShows.TvShowsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<MovieDetailsResponse>

    @GET("/3/movie/{movieId}/images")
    suspend fun getRelatedImages(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<RelatedImagesResponse>

    @GET("/3/movie/{movieId}/credits")
    suspend fun getCredits(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<CreditsResponse>
}