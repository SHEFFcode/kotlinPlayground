package org.kotlinspring.coursecatalogservice.repository

import org.kotlinspring.coursecatalogservice.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository: CrudRepository<Course, Int> {
}