package org.kotlinspring.coursecatalogservice.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class GreetingControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun testRetrieveGreeting() {
        val name = "Jeremy"
        val res = webTestClient.get()
            .uri("/v1/greetings/{name}", name)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java).isEqualTo("Hello, $name!, Hello from default profile")
            .returnResult()

        Assertions.assertEquals(HttpStatus.OK, res.status)
    }

}