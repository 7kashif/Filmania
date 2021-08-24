package com.example.filmaxtesting.dataClasses.personDetails.personRelatedMovieAndShows

data class PersonRelatedMultiResponse(
    val cast: List<PersonCast>,
    val crew: List<PersonCrew>,
    val id: Int
)