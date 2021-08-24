package com.example.filmaxtesting.dataClasses.personDetails

import com.example.filmaxtesting.dataClasses.relatedImages.Backdrop

data class PersonRelatedImagesResponse(
    val id: Int,
    val profiles: List<Backdrop>
)