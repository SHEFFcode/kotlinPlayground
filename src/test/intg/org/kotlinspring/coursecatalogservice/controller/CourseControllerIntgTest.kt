package org.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kotlinspring.coursecatalogservice.dto.CourseDTO
import org.kotlinspring.coursecatalogservice.entity.Course
import org.kotlinspring.coursecatalogservice.repository.CourseRepository
import org.kotlinspring.coursecatalogservice.repository.InstructorRepository
import org.kotlinspring.coursecatalogservice.service.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @Autowired
    private lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setup() {
        // DB cleanup
        courseRepository.deleteAll()
        instructorRepository.deleteAll()
        // Create stub data
        val instructor = instructorEntity()
        val courses = courseEntityList(instructor = instructor)
        // Persist stub data into in memory DB
        instructorRepository.save(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    internal fun addCourse() {
        val instructor = instructorRepository.findAll().first() // There will only be one anyway
        val courseDto = CourseDTO(
            id = null,
            name = "Build restful api with boot and kotlin",
            category = "Kotlin",
            instructorId=instructor.id
        )

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertTrue { savedCourseDTO?.id != null }
    }

    @Test
    internal fun getAllCourses() {
        val courseDTOS = webTestClient.get().uri("/v1/courses").exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("Course DTOs are $courseDTOS")
        assertEquals(3, courseDTOS?.size)
    }

    @Test
    internal fun getAllCourses_byName() {
        val uriString = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("courseName", "SpringBoot")
            .toUriString()


        val courseDTOS = webTestClient.get().uri(uriString).exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("Course DTOs are $courseDTOS")
        assertEquals(2, courseDTOS?.size)
    }

    @Test
    internal fun updateCourse() {
        val instructor = instructorRepository.findAll().first() // There will only be one anyway
        val course = Course(null, "Build restful apis with kotlin", "Development", instructor)
        courseRepository.save(course)

        val updatedCourse = webTestClient.put().uri("/v1/courses/{courseId}", course.id)
            .bodyValue(CourseDTO(null, name = "Updated!", category = "Kotlin", instructorId=instructor.id))
            .exchange()
            .expectStatus().isAccepted
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Updated!", updatedCourse?.name)
    }

    @Test
    internal fun deleteCourse() {
        val instructor = instructorRepository.findAll().first() // There will only be one anyway
        val course = Course(null, "Build apis with Kotlin", "Development", instructor)
        courseRepository.save(course)

        webTestClient.delete().uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus()
            .isNoContent
    }
}