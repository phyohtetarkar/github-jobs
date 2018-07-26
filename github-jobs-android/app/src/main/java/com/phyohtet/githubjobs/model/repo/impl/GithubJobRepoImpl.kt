package com.phyohtet.githubjobs.model.repo.impl

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.api.service.GithubJobService
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.GithubJobRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubJobRepoImpl(private val service: GithubJobService) : GithubJobRepo {

    override fun findPositions(description: String, location: String, fullTime: Boolean, page: Int): LiveData<DataSource<List<JobPositionDTO>>> {
        val liveData = MutableLiveData<DataSource<List<JobPositionDTO>>>()
        liveData.value = DataSource.loading()

        val call = if (fullTime) {
            service.findPositions(description, location, fullTime, page)
        } else {
            service.findPositions(description, location, page)
        }

        call.enqueue(object : Callback<List<JobPositionDTO>> {

            override fun onResponse(call: Call<List<JobPositionDTO>>?, response: Response<List<JobPositionDTO>>?) {
                response?.also {
                    if (it.isSuccessful) {
                        liveData.postValue(DataSource.success(it.body()))
                    } else {
                        liveData.postValue(DataSource.error(it.message()))
                    }
                }
            }

            override fun onFailure(call: Call<List<JobPositionDTO>>?, t: Throwable?) {
                liveData.postValue(DataSource.error(t?.message))
            }

        })

        return liveData
    }

    override fun getPosition(id: String): LiveData<DataSource<JobPositionDTO>> {
        val liveData = MutableLiveData<DataSource<JobPositionDTO>>()
        liveData.value = DataSource.loading()

        service.getPosition(id.plus(".json")).enqueue(object : Callback<JobPositionDTO> {

            override fun onResponse(call: Call<JobPositionDTO>?, response: Response<JobPositionDTO>?) {
                response?.also {
                    if (it.isSuccessful) {
                        liveData.postValue(DataSource.success(it.body()))
                    } else {
                        liveData.postValue(DataSource.error(it.message()))
                    }
                }
            }

            override fun onFailure(call: Call<JobPositionDTO>?, t: Throwable?) {
                liveData.postValue(DataSource.error(t?.message))
            }

        })

        return liveData
    }

}