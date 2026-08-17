package no.digdir.accessrequestapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fdk")
data class FdkProperties(val timeout: Long, val api: String, val frontend: String)
