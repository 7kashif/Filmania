package com.example.filmaxtesting.dataClasses.movies.upComingMovies

import com.example.filmaxtesting.dataClasses.movies.Movies

data class UpComingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<Movies>,
    val total_pages: Int,
    val total_results: Int
)