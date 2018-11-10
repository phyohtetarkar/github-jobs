package com.phyohtet.githubjobs.model.repo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.phyohtet.githubjobs.model.JobPositionSearch
import com.phyohtet.githubjobs.model.NetworkResource
import com.phyohtet.githubjobs.model.api.GithubJobApi
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.detail.JobPositionDetailDataSource
import com.phyohtet.githubjobs.model.repo.positions.JobPositionsDataSourceFactory
import java.util.concurrent.Executor

class GithubJobRepo(
        private val api: GithubJobApi,
        private val executor: Executor) {

    fun findPositions(search: JobPositionSearch): NetworkResource<PagedList<JobPositionDTO>> {
        val sourceFactory = JobPositionsDataSourceFactory(api, search, executor)
        val livePagedList = sourceFactory.toLiveData(pageSize = 20, fetchExecutor = executor)
        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return NetworkResource(
                liveData = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState)
    }

    fun getPosition(id: String): NetworkResource<JobPositionDTO> {
        val source = JobPositionDetailDataSource(api, id, executor)
        val liveData = MutableLiveData<JobPositionDTO>()
        source.load(liveData)

        return NetworkResource(
                liveData = liveData,
                networkState = source.networkState,
                retry = {
                    source.retryFailed()
                },
                refresh = { },
                refreshState = source.networkState)
    }

}