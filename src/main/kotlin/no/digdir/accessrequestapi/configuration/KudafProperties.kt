package no.digdir.accessrequestapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("kudaf")
data class KudafProperties(
    val timeout: Long,
    val soknadApi: String
)
