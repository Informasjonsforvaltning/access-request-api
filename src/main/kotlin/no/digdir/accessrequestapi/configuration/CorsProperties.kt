package no.digdir.accessrequestapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.cors")
data class CorsProperties(val originPatterns: List<String>)
