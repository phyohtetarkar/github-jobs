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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JobPositionDTO

        if (id != other.id) return false
        if (createdAt != other.createdAt) return false
        if (title != other.title) return false
        if (location != other.location) return false
        if (type != other.type) return false
        if (description != other.description) return false
        if (howToApply != other.howToApply) return false
        if (company != other.company) return false
        if (companyUrl != other.companyUrl) return false
        if (companyLogo != other.companyLogo) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + howToApply.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + companyUrl.hashCode()
        result = 31 * result + companyLogo.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }


}