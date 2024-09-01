package org.kotlinspring.coursecatalogservice.controller

import org.kotlinspring.coursecatalogservice.dto.CourseDTO
import org.kotlinspring.coursecatalogservice.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/v1/courses"])
class CourseController(val courseService: CourseService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO): CourseDTO {
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses(): List<CourseDTO> {
        return courseService.retrieveAllCourses()
    }

    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateCourse(@RequestBody courseDTO: CourseDTO, @PathVariable("courseId") courseId: Int): CourseDTO {
        return courseService.updateCourse(courseId, courseDTO)
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("courseId") courseId: Int) {
        return courseService.deleteCourse(courseId)
    }
}