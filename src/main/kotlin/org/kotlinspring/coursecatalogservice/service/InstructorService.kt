package org.kotlinspring.coursecatalogservice.service

import org.kotlinspring.coursecatalogservice.dto.InstructorDTO
import org.kotlinspring.coursecatalogservice.entity.Instructor
import org.kotlinspring.coursecatalogservice.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        instructorRepository.save(instructorEntity)

        return instructorEntity.let { InstructorDTO(it.id, it.name) }
    }

    fun findByInstructorId(instructorId: Int): Instructor? {
        return instructorRepository.findById(instructorId).orElse(null)
    }
}
