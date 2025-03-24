package no.digdir.accessrequestapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.cors")
data class CorsProperties(
    val originPatterns: String
) {
    fun getOriginList(): List<String> = originPatterns.split(",").map { it.trim() }
}
