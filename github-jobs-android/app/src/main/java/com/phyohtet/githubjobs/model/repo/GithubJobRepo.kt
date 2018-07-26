package com.phyohtet.githubjobs.model.repo

import android.arch.lifecycle.LiveData
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.dto.JobPositionDTO

interface GithubJobRepo : Repository {

    fun findPositions(
            description: String,
            location: String,
            fullTime: Boolean,
            page: Int
    ): LiveData<DataSource<List<JobPositionDTO>>>

    fun getPosition(id: String): LiveData<DataSource<JobPositionDTO>>

}