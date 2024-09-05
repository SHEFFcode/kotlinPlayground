package org.kotlinspring.coursecatalogservice.exceptionhandler

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Component
@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    companion object : KLogging()

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        logger().error("MethodArgumentNotValidException observed: ${ex.message}", ex )
        val errors = ex.bindingResult.allErrors.map { error -> error.defaultMessage!! }.sorted()

        logger().info { "Validation errors are ${errors}" }

        return ResponseEntity(errors.joinToString(",") { it }, headers, HttpStatus.BAD_REQUEST)
    }
}