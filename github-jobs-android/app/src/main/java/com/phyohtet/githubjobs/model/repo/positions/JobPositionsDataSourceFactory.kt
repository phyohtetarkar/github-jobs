package com.phyohtet.githubjobs.model.repo.positions

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.phyohtet.githubjobs.model.JobPositionSearch
import com.phyohtet.githubjobs.model.api.GithubJobApi
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import java.util.concurrent.Executor

class JobPositionsDataSourceFactory(
        private val api: GithubJobApi,
        private val search: JobPositionSearch,
        private val retryExecutor: Executor) : DataSource.Factory<Int, JobPositionDTO>() {
    val sourceLiveData = MutableLiveData<JobPositionsDataSource>()
    override fun create(): DataSource<Int, JobPositionDTO> {
        val source = JobPositionsDataSource(api, search, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }

}