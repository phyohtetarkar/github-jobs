package com.phyohtet.githubjobs.model

enum class Status {
    LOADING, SUCCESS, NOT_FOUND, FAILED
}

data class NetworkState private constructor(
        val status: Status,
        val msg: String? = null) {
    companion object {
        val LOADING = NetworkState(Status.LOADING)
        val LOADED = NetworkState(Status.SUCCESS)
        val NOT_FOUND = NetworkState(Status.NOT_FOUND)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}