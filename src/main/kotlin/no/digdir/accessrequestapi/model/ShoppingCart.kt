package no.digdir.accessrequestapi.model

import java.util.UUID

data class ShoppingCart(
    val orgnr: String,
    val language: DatasetLanguage,
    val hintIsPublic: Boolean,
    val hintIsOrg: Boolean,
    val hintIsPrePublicationData: Boolean,
    val dataDef: DataDef,
) {
    val system: String = "datanorge"

    data class DataDef(
        val urlToResource: String,
        val id: UUID,
        val type: DataResourceType,
        val orgnr: String,
        val resourceName: String,
        val uri: String,
    )
}

enum class DatasetLanguage {
    NN,
    NB,
    EN,
    ;

    override fun toString() = name.lowercase()
}
