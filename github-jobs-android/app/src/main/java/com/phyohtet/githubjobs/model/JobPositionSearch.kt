package com.phyohtet.githubjobs.model

data class JobPositionSearch(
        var description: String = "",
        var location: String = "",
        var fullTimeOnly: Boolean = false
)