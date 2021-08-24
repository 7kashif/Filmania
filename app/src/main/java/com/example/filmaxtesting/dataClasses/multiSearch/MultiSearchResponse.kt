package com.example.filmaxtesting.dataClasses.multiSearch

data class MultiSearchResponse(
    val page: Int,
    val results: List<MultiSearchResults>,
    val total_pages: Int,
    val total_results: Int
)