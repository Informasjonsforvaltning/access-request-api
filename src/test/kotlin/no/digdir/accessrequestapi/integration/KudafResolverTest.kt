package no.digdir.accessrequestapi.integration

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.notFound
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import no.digdir.accessrequestapi.model.DataResourceMetadata
import no.digdir.accessrequestapi.model.DataResourceType
import no.digdir.accessrequestapi.model.DatasetLanguage
import no.digdir.accessrequestapi.model.ShoppingCart
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.wiremock.spring.ConfigureWireMock
import org.wiremock.spring.EnableWireMock
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

@Tag("integration")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableWireMock(ConfigureWireMock(port = 6000))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KudafResolverTest {
    @Autowired
    private lateinit var jacksonObjectMapper: JsonMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `resolve data returns OK`() {
        val language = DatasetLanguage.EN
        val type = DataResourceType.DATASETS
        val id = UUID.randomUUID()

        stubFor(
            get("/${type.toUrlString()}/$id").willReturn(
                okJson(jacksonObjectMapper.writeValueAsString(dataResourceMetadata(id, type))),
            ),
        )

        mockMvc
            .post("/datadef-resolver/$language") {
                contentType = MediaType.APPLICATION_JSON
                content = jacksonObjectMapper.writeValueAsString(dataDef(id, type))
            }.andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
    }

    @Test
    fun `resolve data returns not found on missing dataset`() {
        val language = DatasetLanguage.EN
        val type = DataResourceType.DATASETS
        val id = UUID.randomUUID()

        stubFor(
            get("/${type.toUrlString()}/$id").willReturn(
                notFound(),
            ),
        )

        mockMvc.post("/access-request/$language/${type.toUrlString()}/$id").andExpect {
            status { isNotFound() }
        }
    }

    private fun dataDef(id: UUID, type: DataResourceType) = ShoppingCart.DataDef(
        urlToResource = "",
        id = id,
        type = type,
        orgnr = "12345",
        resourceName = "resource",
        uri = "http://localhost:6000/datasets/$id",
    )

    private fun dataResourceMetadata(id: UUID, type: DataResourceType) = DataResourceMetadata(
        title =
        DataResourceMetadata.LocalizedStrings(
            en = "title",
            nb = null,
            nn = null,
            no = null,
        ),
        publisher =
        DataResourceMetadata.Publisher(
            id = "12345",
        ),
        id = id,
        uri = "http://localhost:6000/datasets/$id",
        type = type,
        accessRequestUrl = null,
        contactPoint = null,
        accessRights = null,
        description = null,
        distribution = null,
    )
}
