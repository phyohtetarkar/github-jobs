package com.phyohtet.githubjobs.model.repo.impl

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.api.service.GithubJobService
import com.phyohtet.githubjobs.model.dto.PositionDTO
import com.phyohtet.githubjobs.model.repo.GithubJobRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubJobRepoImpl(private val service: GithubJobService) : GithubJobRepo {

    override fun findPositions(description: String, location: String, fullTime: Boolean): LiveData<DataSource<List<PositionDTO>>> {
        val liveData = MutableLiveData<DataSource<List<PositionDTO>>>()
        liveData.value = DataSource.loading()

        val call = if (fullTime) {
            service.findPositions(description, location, fullTime)
        } else {
            service.findPositions(description, location)
        }

        call.enqueue(object : Callback<List<PositionDTO>> {

            override fun onResponse(call: Call<List<PositionDTO>>?, response: Response<List<PositionDTO>>?) {
                response?.also {
                    if (it.isSuccessful) {
                        liveData.postValue(DataSource.success(it.body()))
                    } else {
                        liveData.postValue(DataSource.error(it.message()))
                    }
                }
            }

            override fun onFailure(call: Call<List<PositionDTO>>?, t: Throwable?) {
                liveData.postValue(DataSource.error(t?.message))
            }

        })

        return liveData
    }

    override fun getPosition(id: String): LiveData<DataSource<PositionDTO>> {
        val liveData = MutableLiveData<DataSource<PositionDTO>>()
        liveData.value = DataSource.loading()

        service.getPosition(id.plus(".json")).enqueue(object : Callback<PositionDTO> {

            override fun onResponse(call: Call<PositionDTO>?, response: Response<PositionDTO>?) {
                response?.also {
                    if (it.isSuccessful) {
                        liveData.postValue(DataSource.success(it.body()))
                    } else {
                        liveData.postValue(DataSource.error(it.message()))
                    }
                }
            }

            override fun onFailure(call: Call<PositionDTO>?, t: Throwable?) {
                liveData.postValue(DataSource.error(t?.message))
            }

        })

        return liveData
    }

}