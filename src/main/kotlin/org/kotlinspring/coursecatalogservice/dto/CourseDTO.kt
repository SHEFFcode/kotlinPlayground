package org.kotlinspring.coursecatalogservice.dto

import jakarta.validation.constraints.NotBlank

data class CourseDTO(
    val id: Int?,
    @get:NotBlank(message = "CourseDTO.name cannot be blank.")
    val name: String,
    @get:NotBlank(message = "CourseDTO.category cannot be blank.")
    val category: String,
)