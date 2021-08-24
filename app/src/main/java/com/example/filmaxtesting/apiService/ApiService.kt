package com.example.filmaxtesting.apiService

import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResponse
import com.example.filmaxtesting.dataClasses.personDetails.PersonDetailsResponse
import com.example.filmaxtesting.dataClasses.personDetails.PersonRelatedImagesResponse
import com.example.filmaxtesting.dataClasses.relatedImages.RelatedImagesResponse
import com.example.filmaxtesting.dataClasses.showsDetails.ShowDetailsResponse
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
    suspend fun getMovieRelatedImages(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<RelatedImagesResponse>

    @GET("/3/movie/{movieId}/credits")
    suspend fun getMovieCredits(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<CreditsResponse>

    @GET("/3/movie/{movieId}/similar")
    suspend fun getSimilarMovies(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<MoviesResponse>

    @GET("/3/tv/{showId}")
    suspend fun getShowDetails(
        @Path("showId") showId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<ShowDetailsResponse>

    @GET("/3/tv/{showId}/images")
    suspend fun getShowRelatedImages(
        @Path("showId") showId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<RelatedImagesResponse>

    @GET("/3/tv/{showId}/credits")
    suspend fun getShowCredits(
        @Path("showId") showId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<CreditsResponse>

    @GET("/3/tv/{showId}/similar")
    suspend fun getSimilarShows(
        @Path("showId") showId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<TvShowsResponse>

    @GET("/3/search/multi")
    suspend fun multiSearch(
        @Query("api_key") api_key:String=Constants.API_KEY,
        @Query("query") query:String,
        @Query("page") page:Int
    ):Response<MultiSearchResponse>

    @GET("/3/person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") person_id:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<PersonDetailsResponse>

    @GET("/3/person/{person_id}/images")
    suspend fun getPersonRelatedImages(
        @Path("person_id") person_id:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<PersonRelatedImagesResponse>

}