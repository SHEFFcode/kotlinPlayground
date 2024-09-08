package org.kotlinspring.coursecatalogservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CourseDTO(
    val id: Int?,
    @get:NotBlank(message = "CourseDTO.name cannot be blank.")
    val name: String,
    @get:NotBlank(message = "CourseDTO.category cannot be blank.")
    val category: String,
    @get:NotNull(message = "CourseDTO.instructorId cannot be blank.")
    val instructorId: Int? = null,
)