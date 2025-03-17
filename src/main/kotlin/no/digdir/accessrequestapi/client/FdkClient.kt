package no.digdir.accessrequestapi.client

import no.digdir.accessrequestapi.configuration.FdkProperties
import no.digdir.accessrequestapi.model.DataResourceMetadata
import org.slf4j.Logger
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Duration
import java.util.*

@Component
class FdkClient(restClientBuilder: RestClient.Builder, private val fdkProperties: FdkProperties) {
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    private val settings: ClientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.defaults()
        .withReadTimeout(Duration.ofSeconds(fdkProperties.timeout))

    private val requestFactory: ClientHttpRequestFactory = ClientHttpRequestFactoryBuilder.detect()
        .build(settings)

    private val restClient = restClientBuilder
        .baseUrl(fdkProperties.api)
        .requestFactory(requestFactory)
        .build()

    fun getMetadata(type: String, id: UUID): DataResourceMetadata? {
        logger.info("Fetching metadata for type: $type and id: $id from ${fdkProperties.api}")

        return restClient.get()
            .uri("/$type/$id")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ status -> status.isError }) { _, response ->
                logger.error("Error fetching metadata for type: $type and id: $id from ${fdkProperties.api} (${response.statusCode})")
            }
            .toEntity(DataResourceMetadata::class.java)
            .body
    }
}
