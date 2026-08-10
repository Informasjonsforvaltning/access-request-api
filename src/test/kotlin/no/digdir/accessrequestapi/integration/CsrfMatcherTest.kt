import jakarta.servlet.http.HttpServletRequest
import no.digdir.accessrequestapi.configuration.SecurityConfig
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CsrfMatcherTest {
    private val allowedOrigins =
        listOf(
            "https://staging.fellesdatakatalog.digdir.no",
            "https://*.staging.fellesdatakatalog.digdir.no",
            "http://localhost:*",
            "https://frontend-soknad-staging.paas2.uninett.no",
        )

    private val matcher = SecurityConfig.CsrfRequiredMatcher(allowedOrigins)

    @Test
    fun `should match exact origin`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Origin")).thenReturn("https://staging.fellesdatakatalog.digdir.no")

        assertTrue(matcher.matches(request))
    }

    @Test
    fun `should match exact referer`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Referer")).thenReturn("https://staging.fellesdatakatalog.digdir.no")

        assertTrue(matcher.matches(request))
    }

    @Test
    fun `should match wildcard origin`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Origin")).thenReturn("https://*.staging.fellesdatakatalog.digdir.no")

        assertTrue(matcher.matches(request))
    }

    @Test
    fun `should match wildcard referer`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Referer")).thenReturn("https://*.staging.fellesdatakatalog.digdir.no")

        assertTrue(matcher.matches(request))
    }

    @Test
    fun `should not match unauthorized origin`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Origin")).thenReturn("https://example.com")

        assertFalse(matcher.matches(request))
    }

    @Test
    fun `should not match unauthorized referer`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Referer")).thenReturn("https://example.com")

        assertFalse(matcher.matches(request))
    }

    @Test
    fun `should not match if no origin and referer`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Origin")).thenReturn(null)
        `when`(request.getHeader("Referer")).thenReturn(null)

        assertFalse(matcher.matches(request))
    }

    @Test
    fun `should not match if origin and referer are empty`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader("Origin")).thenReturn("")
        `when`(request.getHeader("Referer")).thenReturn("")

        assertFalse(matcher.matches(request))
    }
}
