package com.example.filmaxtesting.dataClasses.relatedImages

data class RelatedImagesResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)