package no.digdir.accessrequestapi.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalErrorHandler {
    @ExceptionHandler(HttpClientErrorException::class)
    fun handleClientError(exception: HttpClientErrorException): ResponseEntity<String> =
        ResponseEntity.status(exception.statusCode).body(exception.message)

    @ExceptionHandler(HttpServerErrorException::class)
    fun handleServerError(exception: HttpServerErrorException): ResponseEntity<String> = when (exception.statusCode) {
        HttpStatus.INTERNAL_SERVER_ERROR -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(exception.message)
        HttpStatus.GATEWAY_TIMEOUT -> ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(exception.message)
        else -> ResponseEntity.internalServerError().body(exception.message)
    }

    // Unknown paths raise NoResourceFoundException; without this handler the catch-all below
    // would answer them with 500. The body is left empty so the request path is not reflected.
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResource(): ResponseEntity<Void> = ResponseEntity.notFound().build()

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<String> = ResponseEntity.internalServerError().body(exception.message)
}
