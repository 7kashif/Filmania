package com.example.filmaxtesting.dataClasses.popularPeople

data class Person(
    val adult: Boolean,
    val id: Int,
    val known_for: List<KnownFor>,
    val name: String,
    val popularity: Double,
    val profile_path: String
)