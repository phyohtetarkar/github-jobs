package com.phyohtet.githubjobs.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.phyohtet.githubjobs.model.api.DateDeserializer
import java.util.*

class JobPositionDTO {
    var id: String = ""
    @JsonDeserialize(using = DateDeserializer::class)
    @JsonProperty("created_at")
    val createdAt: Date? = null
    val title: String = ""
    val location: String = ""
    val type: String = ""
    val description: String = ""
    @JsonProperty("how_to_apply")
    val howToApply: String = ""
    val company: String = ""
    @JsonProperty("company_url")
    val companyUrl: String = ""
    @JsonProperty("company_logo")
    val companyLogo: String = ""
    val url: String = ""
}