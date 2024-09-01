package org.kotlinspring.coursecatalogservice.service

import mu.KLogging
import org.kotlinspring.coursecatalogservice.dto.CourseDTO
import org.kotlinspring.coursecatalogservice.entity.Course
import org.kotlinspring.coursecatalogservice.exception.CourseNotFoundException
import org.kotlinspring.coursecatalogservice.repository.CourseRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseService(
    val courseRepository: CourseRepository,
) {
    companion object: KLogging()
    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category)
        }
        courseRepository.save(courseEntity)

        logger().info { "Created new course with id ${courseEntity.id}" }

        // Guaranteed to have an id here if save is successful
        return courseDTO.copy(id = courseEntity.id)
    }

    fun retrieveAllCourses(): List<CourseDTO> {
        return courseRepository.findAll().map { CourseDTO(it.id, it.name, it.category) }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse: Optional<Course> = courseRepository.findById(courseId)
        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category
                courseRepository.save(it)
                CourseDTO(it.id, it.name, it.category)
            }
        } else {
            throw CourseNotFoundException("No course found with id $courseId")
        }

    }

    fun deleteCourse(courseId: Int) {
        val existingCourse: Optional<Course> = courseRepository.findById(courseId)
        if (existingCourse.isPresent) {
            courseRepository.deleteById(courseId)
        } else {
            throw CourseNotFoundException("No course found with id $courseId")
        }
    }
}
