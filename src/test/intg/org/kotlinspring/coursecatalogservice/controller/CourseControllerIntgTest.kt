package org.kotlinspring.coursecatalogservice.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kotlinspring.coursecatalogservice.controller.util.courseEntityList
import org.kotlinspring.coursecatalogservice.dto.CourseDTO
import org.kotlinspring.coursecatalogservice.repository.CourseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
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

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll() // DB cleanup
        val courses = courseEntityList()
        courseRepository.saveAll(courses) // Save expected data

    }

    @Test
    internal fun addCourse() {
        val courseDto = CourseDTO(id = null, name = "Build restful api with boot and kotlin", category = "Kotlin")
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
    internal fun updateCourse() {
        val updatedCourse = webTestClient.put().uri("/v1/courses/1")
            .bodyValue(CourseDTO(null, name = "Updated!", category = "Kotlin"))
            .exchange()
            .expectStatus().isAccepted
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Updated!", updatedCourse?.name)
    }

    @Test
    internal fun deleteCourse() {
        webTestClient.delete().uri("/v1/courses/1")
            .exchange()
            .expectStatus()
            .isNoContent
    }
}