package org.kotlinspring.coursecatalogservice.service

import mu.KLogging
import org.kotlinspring.coursecatalogservice.dto.CourseDTO
import org.kotlinspring.coursecatalogservice.entity.Course
import org.kotlinspring.coursecatalogservice.exception.CourseNotFoundException
import org.kotlinspring.coursecatalogservice.exception.InstructorNotValidException
import org.kotlinspring.coursecatalogservice.repository.CourseRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService,
) {
    companion object: KLogging()
    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        // ensure that instructor exists before adding a course
        val courseInstructor = instructorService.findByInstructorId(courseDTO.instructorId!!)
            ?: throw InstructorNotValidException("Instructor with id ${courseDTO.instructorId} not found")

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructor = courseInstructor)
        }
        courseRepository.save(courseEntity)

        logger().info { "Created new course with id ${courseEntity.id}" }

        // Guaranteed to have an id here if save is successful
        return courseDTO.copy(id = courseEntity.id)
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        val requestedCourses = courseName?.let {
            courseRepository.findCoursesByNameContaining(it)
        } ?: courseRepository.findAll()

        return requestedCourses.map { CourseDTO(it.id, it.name, it.category, it.instructor!!.id) }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse: Optional<Course> = courseRepository.findById(courseId)
        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category
                courseRepository.save(it)
                CourseDTO(it.id, it.name, it.category, it.instructor!!.id!!)
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
