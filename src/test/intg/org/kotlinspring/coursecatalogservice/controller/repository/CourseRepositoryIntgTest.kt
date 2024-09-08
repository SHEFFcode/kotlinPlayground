package org.kotlinspring.coursecatalogservice.controller.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kotlinspring.coursecatalogservice.controller.util.courseEntityList
import org.kotlinspring.coursecatalogservice.repository.CourseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream
import kotlin.test.assertEquals

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {
    @Autowired
    private lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll() // DB cleanup
        val courses = courseEntityList()
        courseRepository.saveAll(courses) // Save expected data

    }

    @Test
    fun testFindByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")
        assertThat(courses).isNotEmpty
        assertEquals(2, courses.size)
    }

    @Test
    fun testFindCoursesByNameContaining() {
        val courses = courseRepository.findCoursesByNameContaining("SpringBoot")
        assertThat(courses).isNotEmpty
        assertEquals(2, courses.size)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun testFindCoursesByName_parameterized(courseName: String, expectedSize: Int) {
        val courses = courseRepository.findCoursesByNameContaining(courseName)
        assertThat(courses).isNotEmpty
        assertEquals(expectedSize, courses.size)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Wiremock", 1),
            )
        }
    }
}