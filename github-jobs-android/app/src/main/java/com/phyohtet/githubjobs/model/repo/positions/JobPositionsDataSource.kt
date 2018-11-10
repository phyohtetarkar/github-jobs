package com.phyohtet.githubjobs.model.repo.positions

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.phyohtet.githubjobs.model.JobPositionSearch
import com.phyohtet.githubjobs.model.NetworkState
import com.phyohtet.githubjobs.model.api.GithubJobApi
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import java.io.IOException
import java.util.concurrent.Executor

class JobPositionsDataSource(
        private val api: GithubJobApi,
        private val search: JobPositionSearch,
        private val retryExecutor: Executor) : PageKeyedDataSource<Int, JobPositionDTO>() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.also {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, JobPositionDTO>) {
        val call = if (search.fullTimeOnly) {
            api.findPositions(search.description, search.location, search.fullTimeOnly, 0)
        } else {
            api.findPositions(search.description, search.location, 0)
        }

        initialLoad.postValue(NetworkState.LOADING)

        try {
            val resp = call.execute()
            if (resp.isSuccessful) {
                val positions = resp.body() ?: emptyList()

                retry = null
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(positions, 0, 1)
            } else {
                retry = {
                    loadInitial(params, callback)
                }
                initialLoad.postValue(NetworkState.error("Network Error."))
            }
        } catch (e: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            initialLoad.postValue(NetworkState.error("Network Error."))
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, JobPositionDTO>) {
        val call = if (search.fullTimeOnly) {
            api.findPositions(search.description, search.location, search.fullTimeOnly, params.key)
        } else {
            api.findPositions(search.description, search.location, params.key)
        }

        networkState.postValue(NetworkState.LOADING)

        try {
            val resp = call.execute()
            if (resp.isSuccessful) {
                val positions = resp.body() ?: emptyList()

                retry = null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(positions, params.key + 1)
            } else {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error("Network Error."))
            }
        } catch (e: IOException) {
            retry = {
                loadAfter(params, callback)
            }
            networkState.postValue(NetworkState.error("Network Error."))
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, JobPositionDTO>) {
    }

}