package no.digdir.accessrequestapi.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(CaseInsensitiveEnumConverterFactory())
    }
}

private class CaseInsensitiveEnumConverterFactory : ConverterFactory<String, Enum<*>> {
    override fun <T : Enum<*>> getConverter(targetType: Class<T>): Converter<String, T> =
        Converter { source ->
            targetType.enumConstants.firstOrNull { it.name.equals(source.trim(), ignoreCase = true) }
                ?: throw IllegalArgumentException(
                    "No enum constant ${targetType.canonicalName}.$source",
                )
        }
}
