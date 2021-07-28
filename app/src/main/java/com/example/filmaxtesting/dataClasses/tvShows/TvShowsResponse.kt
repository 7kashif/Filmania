package com.example.filmaxtesting.dataClasses.tvShows

data class TvShowsResponse(
    val page: Int,
    val results: List<TvShows>,
    val total_pages: Int,
    val total_results: Int
)
