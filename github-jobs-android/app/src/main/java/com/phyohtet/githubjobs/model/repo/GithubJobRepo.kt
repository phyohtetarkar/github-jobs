package com.phyohtet.githubjobs.model.repo

import android.arch.lifecycle.LiveData
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.dto.PositionDTO

interface GithubJobRepo : Repository {

    fun findPositions(description: String, location: String, fullTime: Boolean): LiveData<DataSource<List<PositionDTO>>>

    fun getPosition(id: String): LiveData<DataSource<PositionDTO>>

}