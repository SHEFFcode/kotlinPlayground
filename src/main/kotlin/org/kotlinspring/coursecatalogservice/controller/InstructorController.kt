package org.kotlinspring.coursecatalogservice.controller

import org.kotlinspring.coursecatalogservice.dto.InstructorDTO
import org.kotlinspring.coursecatalogservice.entity.Instructor
import org.kotlinspring.coursecatalogservice.service.InstructorService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(val instructorService: InstructorService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@RequestBody instructorDTO: InstructorDTO): InstructorDTO {
        return instructorService.createInstructor(instructorDTO)
    }
}