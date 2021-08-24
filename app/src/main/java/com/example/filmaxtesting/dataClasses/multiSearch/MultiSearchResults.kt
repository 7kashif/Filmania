package com.example.filmaxtesting.dataClasses.multiSearch

data class MultiSearchResults(
    val adult: Boolean,
    val backdrop_path: String?,
    val first_air_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val known_for: List<KnownFor>,
    val media_type: String,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val original_title: String,
    val overview: String,
    val popularity: Int,
    val poster_path: String?,
    val profile_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Int,
    val vote_count: Int
)