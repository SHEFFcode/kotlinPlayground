package org.kotlinspring.catalogservice.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.kotlinspring.coursecatalogservice.controller.GreetingController
import org.kotlinspring.coursecatalogservice.service.GreetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [GreetingController::class])
@AutoConfigureWebTestClient(timeout = "15m")
class GreetingControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var greetingServiceMock: GreetingService

    @Test
    fun testRetrieveGreeting() {
        // Setup
        val name = "Jeremy"
        every { greetingServiceMock.retrieveGreeting(any()) } returns "Hello, $name!, Hello from default profile"

        // Run
        val res = webTestClient.get()
            .uri("/v1/greetings/{name}", name)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java).isEqualTo("Hello, $name!, Hello from default profile")
            .returnResult()

        // Assert
        Assertions.assertEquals(HttpStatus.OK, res.status)
    }
}