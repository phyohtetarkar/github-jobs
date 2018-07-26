package com.phyohtet.githubjobs.model

class DataSource<out T>(
        val status: Status,
        val data: T?,
        val message: String?
) {

    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    companion object {

        fun <T> loading(): DataSource<T> {
            return DataSource(Status.LOADING, null, null)
        }

        fun <T> success(data: T?): DataSource<T> {
            return DataSource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String?): DataSource<T> {
            return DataSource(Status.ERROR, null, message)
        }

    }

}