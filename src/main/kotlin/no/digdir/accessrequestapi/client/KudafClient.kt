package no.digdir.accessrequestapi.client

import no.digdir.accessrequestapi.configuration.KudafProperties
import no.digdir.accessrequestapi.model.ShoppingCart
import org.slf4j.Logger
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.HttpClientSettings
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Duration

@Component
class KudafClient(private val kudafProperties: KudafProperties) {
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    private val settings: HttpClientSettings = HttpClientSettings.defaults()
        .withReadTimeout(Duration.ofSeconds(kudafProperties.timeout))

    private val requestFactory: ClientHttpRequestFactory = ClientHttpRequestFactoryBuilder.detect()
        .build(settings)

    private val restClient = RestClient.builder()
        .baseUrl(kudafProperties.soknadApi)
        .requestFactory(requestFactory)
        .build()

    fun getRedirectUrl(cart: ShoppingCart): String? {
        logger.info("Fetching redirect URL for cart: $cart from ${kudafProperties.soknadApi}")

        return restClient.post()
            .uri("/cart")
            .accept(MediaType.APPLICATION_JSON)
            .body(cart)
            .retrieve()
            .onStatus({ status -> status.isError }) { _, response ->
                logger.error("Error fetching redirect URL for cart: $cart from ${kudafProperties.soknadApi} (${response.statusCode})")
            }
            .body(KudafAccessRequestResponse::class.java)
            ?.redirectUrl
    }
}

data class KudafAccessRequestResponse(val redirectUrl: String)
