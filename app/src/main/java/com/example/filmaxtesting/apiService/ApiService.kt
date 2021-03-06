package com.example.filmaxtesting.apiService

import com.example.filmaxtesting.Constants
import com.example.filmaxtesting.dataClasses.credits.CreditsResponse
import com.example.filmaxtesting.dataClasses.movieDetails.MovieDetailsResponse
import com.example.filmaxtesting.dataClasses.movies.MoviesResponse
import com.example.filmaxtesting.dataClasses.movies.upComingMovies.UpComingMoviesResponse
import com.example.filmaxtesting.dataClasses.multiSearch.MultiSearchResponse
import com.example.filmaxtesting.dataClasses.personDetails.PersonDetailsResponse
import com.example.filmaxtesting.dataClasses.personDetails.PersonRelatedImagesResponse
import com.example.filmaxtesting.dataClasses.personDetails.personRelatedMovieAndShows.PersonRelatedMultiResponse
import com.example.filmaxtesting.dataClasses.popularPeople.PopularPeopleResponse
import com.example.filmaxtesting.dataClasses.relatedImages.RelatedImagesResponse
import com.example.filmaxtesting.dataClasses.showsDetails.ShowDetailsResponse
import com.example.filmaxtesting.dataClasses.tvShows.TvShowsResponse
import com.example.filmaxtesting.dataClasses.video.VideosResponse
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
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<MovieDetailsResponse>

    @GET("/3/movie/{movieId}/images")
    suspend fun getMovieRelatedImages(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<RelatedImagesResponse>

    @GET("/3/movie/{movieId}/credits")
    suspend fun getMovieCredits(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<CreditsResponse>

    @GET("/3/movie/{movieId}/similar")
    suspend fun getSimilarMovies(
        @Path("movieId") movieId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<MoviesResponse>

    @GET("/3/tv/{showId}")
    suspend fun getShowDetails(
        @Path("showId") showId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<ShowDetailsResponse>

    @GET("/3/tv/{showId}/images")
    suspend fun getShowRelatedImages(
        @Path("showId") showId:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
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

    @GET ("/3/person/{person_id}/combined_credits")
    suspend fun getPersonRelatedMedia(
        @Path("person_id") person_id:Int,
        @Query("api_key") api_key:String=Constants.API_KEY,
    ):Response<PersonRelatedMultiResponse>

    @GET("/3/person/popular")
    suspend fun getPopularPeople(
        @Query("api_key") api_key:String= Constants.API_KEY,
        @Query("page") page:Int
    ):Response<PopularPeopleResponse>

    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key:String= Constants.API_KEY,
        @Query("page") page:Int
    ):Response<UpComingMoviesResponse>

    @GET("/3/trending/{media_type}/{time_window}")
    suspend fun getTrendingMovies(
        @Path("media_type") media_type:String = "movie",
        @Path("time_window") time_window:String="week",
        @Query("api_key") api_key:String= Constants.API_KEY,
        @Query("page") page:Int =1
    ):Response<MoviesResponse>

    @GET("/3/trending/{media_type}/{time_window}")
    suspend fun getTrendingShows(
        @Path("media_type") media_type:String = "tv",
        @Path("time_window") time_window:String="week",
        @Query("api_key") api_key:String= Constants.API_KEY,
        @Query("page") page:Int =1
    ):Response<TvShowsResponse>

    @GET("/3/movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<VideosResponse>

    @GET("/3/tv/{tv_id}/videos")
    suspend fun getShowsVideos(
        @Path("tv_id") tv_id:Int,
        @Query("api_key") api_key:String=Constants.API_KEY
    ):Response<VideosResponse>

}