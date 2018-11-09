package com.phyohtet.githubjobs.model.repo.detail

import androidx.lifecycle.MutableLiveData
import com.phyohtet.githubjobs.model.NetworkState
import com.phyohtet.githubjobs.model.api.GithubJobApi
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class JobPositionDetailDataSource(
        private val api: GithubJobApi,
        private val id: String,
        private val retryExecutor: Executor) {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    fun retryFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.also {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    fun load(liveData: MutableLiveData<JobPositionDTO>) {
        val call = api.getPosition(id.plus(".json"))
        networkState.postValue(NetworkState.LOADING)

        call.enqueue(object : Callback<JobPositionDTO> {
            override fun onFailure(call: Call<JobPositionDTO>, t: Throwable) {
                retry = {
                    load(liveData)
                }
                networkState.postValue(NetworkState.error(t.message))
            }

            override fun onResponse(call: Call<JobPositionDTO>, response: Response<JobPositionDTO>) {
                if (response.isSuccessful) {
                    retry = null
                    liveData.postValue(response.body())
                    networkState.postValue(NetworkState.LOADED)
                } else {
                    retry = {
                        load(liveData)
                    }
                    networkState.postValue(NetworkState.error("Error Code: ${response.code()}"))
                }
            }
        })
    }

}