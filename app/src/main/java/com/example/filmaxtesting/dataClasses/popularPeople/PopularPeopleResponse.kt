package com.example.filmaxtesting.dataClasses.popularPeople

data class PopularPeopleResponse(
    val page: Int,
    val results: List<Person>,
    val total_pages: Int,
    val total_results: Int
)