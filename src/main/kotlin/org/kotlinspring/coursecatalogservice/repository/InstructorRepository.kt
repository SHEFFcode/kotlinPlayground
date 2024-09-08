package org.kotlinspring.coursecatalogservice.repository

import org.kotlinspring.coursecatalogservice.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {
}