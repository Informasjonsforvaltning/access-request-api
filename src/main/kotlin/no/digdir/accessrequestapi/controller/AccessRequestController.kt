package no.digdir.accessrequestapi.controller

import io.swagger.v3.oas.annotations.tags.Tag
import no.digdir.accessrequestapi.client.FdkClient
import no.digdir.accessrequestapi.client.KudafClient
import no.digdir.accessrequestapi.configuration.FdkProperties
import no.digdir.accessrequestapi.model.DatasetLanguage
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "Access request")
@RestController
@RequestMapping(value = ["/access-request"], produces = ["application/json"])
class AccessRequestController(
    private val fdkProperties: FdkProperties,
    private val fdkClient: FdkClient,
    private val kudafClient: KudafClient
) {
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @PostMapping("/{language}/{type}/{id}")
    fun createKudafApplication(
        @PathVariable language: DatasetLanguage,
        @PathVariable type: String,
        @PathVariable id: UUID
    ): ResponseEntity<String> {
        logger.info("Received request to create Kudaf application for type: $type, id: $id, language: $language")

        val metadata = fdkClient.getMetadata(type, id) ?: return ResponseEntity.notFound().build()

        val shoppingCart =
            metadata.toShoppingCart(urlToResource = "${fdkProperties.frontend}/$type/$id", language = language)

        val redirectUrl = kudafClient.getRedirectUrl(shoppingCart) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(redirectUrl)
    }
}
