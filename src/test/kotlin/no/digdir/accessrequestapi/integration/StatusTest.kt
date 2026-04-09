package no.digdir.accessrequestapi.integration

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@Tag("integration")
@ActiveProfiles("test")

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatusTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `ping request returns OK`() {
        mockMvc.get("/ping").andExpect { status { isOk() } }
    }

    @Test
    fun `ready request returns OK`() {
        mockMvc.get("/ready").andExpect { status { isOk() } }
    }
}
