package org.kotlinspring.coursecatalogservice.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.kotlinspring.coursecatalogservice.controller.CourseController
import org.kotlinspring.coursecatalogservice.dto.CourseDTO
import org.kotlinspring.coursecatalogservice.service.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.junit.jupiter.api.Test
import org.kotlinspring.coursecatalogservice.entity.Course
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient(timeout = "15m")
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun testAddCourse() {
        val courseDTO = CourseDTO(null, "Build a restul api with Kotlin", "Learning", 1)

        every { courseServiceMock.addCourse(any()) } returns courseDTO.copy(id = 1)

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult().responseBody

        assertTrue { savedCourseDTO?.id != null }
    }

    @Test
    fun testAddCourse_validation() {
        val courseDTO = CourseDTO(null, "", "", 1) // validate no empty strings

        every { courseServiceMock.addCourse(any()) } returns courseDTO.copy(id = 1)

        val result = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult().responseBody

        assertEquals("CourseDTO.category cannot be blank.,CourseDTO.name cannot be blank.", result)
    }

    @Test
    fun testAddCourse_runtime_exception() {
        val courseDTO = CourseDTO(null, "Build a restul api with Kotlin", "Learning", 1)

        val errorMessage = "Unexpected error ocurred"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        val result = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult().responseBody

        assertEquals(errorMessage, result)
    }

    @Test
    fun testGetAllCourses() {

        every { courseServiceMock.retrieveAllCourses(any()) } returns listOf(
            CourseDTO(1, "Hello", "Learning"),
            CourseDTO(2, "GoodBye", "Learning"),
        )

        val courseDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult().responseBody

        assertEquals(2, courseDTOs?.size)
    }

    @Test
    fun testUpdateCourse() {
        val courseDTO = CourseDTO(1, "GoodBye", "Learning")
        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO

        val updatedDTO = webTestClient.put()
            .uri("/v1/courses/{courseId}", 1)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isAccepted
            .expectBody(CourseDTO::class.java)
            .returnResult().responseBody

        assertEquals("GoodBye", updatedDTO?.name)
    }

    @Test
    fun testDeleteCourse() {
        every { courseServiceMock.deleteCourse(any()) } returns Unit

        webTestClient.delete()
            .uri("/v1/courses/{courseId}", 1)
            .exchange()
            .expectStatus().isNoContent

    }
}