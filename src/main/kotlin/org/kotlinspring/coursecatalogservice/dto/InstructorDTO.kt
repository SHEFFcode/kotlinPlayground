package org.kotlinspring.coursecatalogservice.dto

import jakarta.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "InstructorDTO.name cannot be blank")
    var name: String,
)